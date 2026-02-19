package com.matthias.dealmonitor.service;

import com.matthias.dealmonitor.marketplace.MarketplaceClient;
import com.matthias.dealmonitor.model.Deal;
import com.matthias.dealmonitor.model.PriceHistory;
import com.matthias.dealmonitor.notify.TelegramNotifier;
import com.matthias.dealmonitor.repo.DealRepository;
import com.matthias.dealmonitor.repo.PriceHistoryRepository;
import com.matthias.dealmonitor.repo.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MonitorScheduler {

    private final WatchlistRepository watchlistRepository;
    private final DealRepository dealRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final MarketplaceClient marketplaceClient;
    private final TelegramNotifier telegramNotifier;

    public MonitorScheduler(WatchlistRepository watchlistRepository,
                            DealRepository dealRepository,
                            PriceHistoryRepository priceHistoryRepository,
                            MarketplaceClient marketplaceClient,
                            TelegramNotifier telegramNotifier) {
        this.watchlistRepository = watchlistRepository;
        this.dealRepository = dealRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.marketplaceClient = marketplaceClient;
        this.telegramNotifier = telegramNotifier;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.delay-ms:60000}")
    @Transactional
    public void tick() {
        var watchlists = watchlistRepository.findByActiveTrue();
        Instant now = Instant.now();

        for (var w : watchlists) {
            var offers = marketplaceClient.search(w.getKeyword(), w.getLocation(), w.getMaxPriceCents());
            for (var o : offers) {
                // Simple filter: only notify if price <= maxPriceCents
                if (o.priceCents() > w.getMaxPriceCents()) continue;

                var existing = dealRepository.findByUserIdAndExternalId(w.getUser().getId(), o.externalId());
                if (existing.isPresent()) {
                    var d = existing.get();
                    d.setLastSeenAt(now);

                    // If price changed, append history
                    if (!d.getPriceCents().equals(o.priceCents())) {
                        d.setPriceCents(o.priceCents());
                        priceHistoryRepository.save(PriceHistory.builder()
                                .deal(d)
                                .priceCents(o.priceCents())
                                .observedAt(now)
                                .build());
                    }
                    continue;
                }

                Deal d = Deal.builder()
                        .user(w.getUser())
                        .externalId(o.externalId())
                        .title(o.title())
                        .priceCents(o.priceCents())
                        .location(o.location())
                        .url(o.url())
                        .firstSeenAt(now)
                        .lastSeenAt(now)
                        .build();
                dealRepository.save(d);

                priceHistoryRepository.save(PriceHistory.builder()
                        .deal(d)
                        .priceCents(d.getPriceCents())
                        .observedAt(now)
                        .build());

                if (telegramNotifier.isEnabled()) {
                    telegramNotifier.send("New deal: " + d.getTitle()
                            + "\nPrice: " + (d.getPriceCents() / 100.0)
                            + "\nLocation: " + (d.getLocation() == null ? "-" : d.getLocation())
                            + "\n" + (d.getUrl() == null ? "" : d.getUrl()));
                }
            }
        }
    }
}
