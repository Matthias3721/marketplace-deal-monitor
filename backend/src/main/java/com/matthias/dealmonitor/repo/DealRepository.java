package com.matthias.dealmonitor.repo;

import com.matthias.dealmonitor.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {
    Optional<Deal> findByUserIdAndExternalId(Long userId, String externalId);
    List<Deal> findTop50ByUserIdOrderByLastSeenAtDesc(Long userId);
}
