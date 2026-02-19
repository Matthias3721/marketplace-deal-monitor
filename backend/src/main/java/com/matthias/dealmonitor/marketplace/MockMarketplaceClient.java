package com.matthias.dealmonitor.marketplace;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockMarketplaceClient implements MarketplaceClient {

    private final boolean enabled;

    public MockMarketplaceClient(@Value("${app.marketplace.mock:true}") boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<MarketplaceOffer> search(String keyword, String location, int maxPriceCents) {
        if (!enabled) return List.of();

        // Generates 0-2 pseudo offers per run to demonstrate the pipeline end-to-end.
        int count = ThreadLocalRandom.current().nextInt(0, 3);
        List<MarketplaceOffer> out = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int price = ThreadLocalRandom.current().nextInt(Math.max(1, maxPriceCents - 20000), maxPriceCents + 10000);
            String id = UUID.randomUUID().toString().substring(0, 8);
            out.add(new MarketplaceOffer(
                    id,
                    keyword + " deal #" + id,
                    price,
                    location != null ? location : "Kraków",
                    "https://example.com/offers/" + id
            ));
        }
        return out;
    }
}
