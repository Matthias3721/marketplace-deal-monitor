package com.matthias.dealmonitor.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        // HS256 needs a sufficiently long secret (>= 32 bytes recommended).
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(bytes.length >= 32 ? bytes : padTo32(bytes));
    }

    private static byte[] padTo32(byte[] src) {
        byte[] out = new byte[32];
        for (int i = 0; i < out.length; i++) out[i] = src[i % src.length];
        return out;
    }

    public String issueToken(String subjectEmail, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(60 * 60 * 24); // 24h
        return Jwts.builder()
                .subject(subjectEmail)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        Object v = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
        return v == null ? null : v.toString();
    }
}
