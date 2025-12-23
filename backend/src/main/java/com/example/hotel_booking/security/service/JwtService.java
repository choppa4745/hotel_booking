package com.example.hotel_booking.security.service;

import com.example.hotel_booking.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-token-ttl-minutes:60}")
    private long ttlMinutes;

    private SecretKey key;
    private JwtParser parser;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(String subject, Role role, UUID employeeId, UUID guestId) {
        Instant now = Instant.now();
        Instant exp = now.plus(ttlMinutes, ChronoUnit.MINUTES);

        // ВАЖНО: Map.of(...) НЕ принимает null — у тебя там падало именно поэтому.
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());
        if (employeeId != null) claims.put("employeeId", employeeId.toString());
        if (guestId != null) claims.put("guestId", guestId.toString());

        return Jwts.builder()
                .setSubject(subject)              // например username или userId
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Role getRole(String token) {
        String r = String.valueOf(parseClaims(token).get("role"));
        return Role.valueOf(r);
    }

    public UUID getEmployeeId(String token) {
        Object v = parseClaims(token).get("employeeId");
        return v == null ? null : UUID.fromString(v.toString());
    }

    public UUID getGuestId(String token) {
        Object v = parseClaims(token).get("guestId");
        return v == null ? null : UUID.fromString(v.toString());
    }
}
