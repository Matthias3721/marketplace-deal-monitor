package com.matthias.dealmonitor.repo;

import com.matthias.dealmonitor.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Watchlist> findByActiveTrue();
}
