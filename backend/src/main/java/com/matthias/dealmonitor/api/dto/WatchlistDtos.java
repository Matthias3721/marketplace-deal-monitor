package com.matthias.dealmonitor.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WatchlistDtos {

    public record WatchlistRequest(
            @NotBlank String keyword,
            String location,
            @NotNull @Min(0) Integer maxPriceCents,
            @NotNull Boolean active
    ) {}

    public record WatchlistResponse(
            Long id,
            String keyword,
            String location,
            Integer maxPriceCents,
            boolean active
    ) {}
}
