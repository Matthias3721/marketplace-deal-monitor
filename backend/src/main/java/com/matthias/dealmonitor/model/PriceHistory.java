package com.matthias.dealmonitor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "price_history")
public class PriceHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Deal deal;

    @Column(nullable = false)
    private Integer priceCents;

    @Column(nullable = false)
    private Instant observedAt;
}
