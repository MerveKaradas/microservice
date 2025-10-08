package com.fintech.userservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Service
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

    
    
}
