package com.matthias.dealmonitor.repo;

import com.matthias.dealmonitor.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
}
