package com.matthias.dealmonitor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "deals", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "externalId"}))
public class Deal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer priceCents;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true, length = 1000)
    private String url;

    @Column(nullable = false)
    private Instant firstSeenAt;

    @Column(nullable = false)
    private Instant lastSeenAt;
}
