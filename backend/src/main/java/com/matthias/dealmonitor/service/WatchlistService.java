package com.matthias.dealmonitor.service;

import com.matthias.dealmonitor.api.dto.WatchlistDtos;
import com.matthias.dealmonitor.model.Watchlist;
import com.matthias.dealmonitor.repo.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final CurrentUserService currentUserService;

    public WatchlistService(WatchlistRepository watchlistRepository, CurrentUserService currentUserService) {
        this.watchlistRepository = watchlistRepository;
        this.currentUserService = currentUserService;
    }

    public List<WatchlistDtos.WatchlistResponse> listMine() {
        var user = currentUserService.requireUser();
        return watchlistRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(w -> new WatchlistDtos.WatchlistResponse(
                        w.getId(), w.getKeyword(), w.getLocation(), w.getMaxPriceCents(), w.isActive()
                ))
                .toList();
    }

    @Transactional
    public WatchlistDtos.WatchlistResponse create(WatchlistDtos.WatchlistRequest req) {
        var user = currentUserService.requireUser();
        Watchlist w = Watchlist.builder()
                .user(user)
                .keyword(req.keyword())
                .location(req.location())
                .maxPriceCents(req.maxPriceCents())
                .active(req.active())
                .createdAt(Instant.now())
                .build();
        watchlistRepository.save(w);
        return new WatchlistDtos.WatchlistResponse(w.getId(), w.getKeyword(), w.getLocation(), w.getMaxPriceCents(), w.isActive());
    }

    @Transactional
    public WatchlistDtos.WatchlistResponse update(Long id, WatchlistDtos.WatchlistRequest req) {
        var user = currentUserService.requireUser();
        Watchlist w = watchlistRepository.findById(id).orElseThrow();
        if (!w.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("Not yours");
        w.setKeyword(req.keyword());
        w.setLocation(req.location());
        w.setMaxPriceCents(req.maxPriceCents());
        w.setActive(req.active());
        return new WatchlistDtos.WatchlistResponse(w.getId(), w.getKeyword(), w.getLocation(), w.getMaxPriceCents(), w.isActive());
    }

    @Transactional
    public void delete(Long id) {
        var user = currentUserService.requireUser();
        Watchlist w = watchlistRepository.findById(id).orElseThrow();
        if (!w.getUser().getId().equals(user.getId())) throw new IllegalArgumentException("Not yours");
        watchlistRepository.delete(w);
    }
}
