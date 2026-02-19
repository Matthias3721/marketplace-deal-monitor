package com.matthias.dealmonitor.service;

import com.matthias.dealmonitor.model.User;
import com.matthias.dealmonitor.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a.getName() == null) throw new IllegalStateException("Not authenticated");
        return userRepository.findByEmail(a.getName().toLowerCase())
                .orElseThrow(() -> new IllegalStateException("User missing in DB"));
    }
}
