package com.fintech.gateway.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

     public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    

    
}
