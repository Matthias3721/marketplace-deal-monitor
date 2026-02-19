package com.matthias.dealmonitor.service;

import com.matthias.dealmonitor.api.dto.AuthDtos;
import com.matthias.dealmonitor.model.Role;
import com.matthias.dealmonitor.model.User;
import com.matthias.dealmonitor.repo.UserRepository;
import com.matthias.dealmonitor.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .email(req.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .createdAt(Instant.now())
                .build();
        userRepository.save(user);

        String token = jwtService.issueToken(user.getEmail(), user.getRole().name());
        return new AuthDtos.AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password())
        );

        User user = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow();

        String token = jwtService.issueToken(user.getEmail(), user.getRole().name());
        return new AuthDtos.AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}
