package com.matthias.dealmonitor.api.dto;

import java.time.Instant;

public class DealDtos {
    public record DealResponse(
            String externalId,
            String title,
            Integer priceCents,
            String location,
            String url,
            Instant lastSeenAt
    ) {}
}
