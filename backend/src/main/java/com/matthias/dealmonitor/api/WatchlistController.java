package com.matthias.dealmonitor.api;

import com.matthias.dealmonitor.api.dto.WatchlistDtos;
import com.matthias.dealmonitor.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public List<WatchlistDtos.WatchlistResponse> listMine() {
        return watchlistService.listMine();
    }

    @PostMapping
    public WatchlistDtos.WatchlistResponse create(@Valid @RequestBody WatchlistDtos.WatchlistRequest req) {
        return watchlistService.create(req);
    }

    @PutMapping("/{id}")
public WatchlistDtos.WatchlistResponse update(@PathVariable("id") Long id,
                                              @Valid @RequestBody WatchlistDtos.WatchlistRequest req) {
    return watchlistService.update(id, req);
}

@DeleteMapping("/{id}")
public void delete(@PathVariable("id") Long id) {
    watchlistService.delete(id);
}

}
