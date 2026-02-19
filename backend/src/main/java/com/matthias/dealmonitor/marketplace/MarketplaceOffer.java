package com.matthias.dealmonitor.marketplace;

public record MarketplaceOffer(
        String externalId,
        String title,
        int priceCents,
        String location,
        String url
) {}
