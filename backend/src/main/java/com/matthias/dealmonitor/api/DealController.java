package com.matthias.dealmonitor.api;

import com.matthias.dealmonitor.api.dto.DealDtos;
import com.matthias.dealmonitor.repo.DealRepository;
import com.matthias.dealmonitor.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealRepository dealRepository;
    private final CurrentUserService currentUserService;

    public DealController(DealRepository dealRepository, CurrentUserService currentUserService) {
        this.dealRepository = dealRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<DealDtos.DealResponse> latest() {
        var user = currentUserService.requireUser();
        return dealRepository.findTop50ByUserIdOrderByLastSeenAtDesc(user.getId()).stream()
                .map(d -> new DealDtos.DealResponse(
                        d.getExternalId(), d.getTitle(), d.getPriceCents(), d.getLocation(), d.getUrl(), d.getLastSeenAt()
                ))
                .toList();
    }
}
