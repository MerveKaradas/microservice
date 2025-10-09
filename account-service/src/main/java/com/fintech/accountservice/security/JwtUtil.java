package com.fintech.accountservice.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();  // subject userId olarak ayarlandı(authservice'de)
    }

    public UUID extractUserId(String token) {
        try {
            return UUID.fromString(extractUsername(token));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("JWT içeriğindeki userId geçersiz UUID formatında değil.");
        }
    }
    
    
}
