package com.matthias.dealmonitor.marketplace;

import java.util.List;

public interface MarketplaceClient {
    List<MarketplaceOffer> search(String keyword, String location, int maxPriceCents);
}
