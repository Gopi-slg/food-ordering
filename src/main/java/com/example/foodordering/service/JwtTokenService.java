package com.example.foodordering.service;

import com.example.foodordering.entity.Customer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {
    private final SecretKey key;
    private final long expirationSeconds;
    public JwtTokenService(@Value("${app.jwt.secret}") String base64Secret,
                           @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds){
        this.key=Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationSeconds=expirationSeconds;
    }
    public String createToken(Customer customer){
        Instant now=Instant.now();

        return Jwts.builder().subject(customer.getEmail())
                .claim("role", customer.getRole().name())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key).compact();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
