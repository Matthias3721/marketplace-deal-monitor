package com.matthias.dealmonitor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "watchlists")
public class Watchlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private Integer maxPriceCents;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Instant createdAt;
}
