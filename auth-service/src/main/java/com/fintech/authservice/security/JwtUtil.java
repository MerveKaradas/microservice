package com.fintech.authservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fintech.authservice.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    
    private final SecretKey secretKey;
    private final long jwtExpirationInMs; 
    private final long refreshExpirationInMs;

    public JwtUtil(@Value("${jwt.secret}") String secret, // String şeklinde alıyoruz dosyadan
                    @Value("${jwt.accessExpiration}") long jwtExpirationInMs, 
                    @Value("${jwt.refreshExpiration}") long refreshExpirationInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // SecretKey'e çeviriyoruz
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.refreshExpirationInMs = refreshExpirationInMs;
    }

    // Acess Token üretimi
    public String generateAccessToken(User user) {
        
        String jti = UUID.randomUUID().toString(); // benzersiz token id olusturuyoturz

        return Jwts.builder()
                .setId(jti)
                .setSubject(user.getId().toString())
                .claim("role", user.getRole().name()) // token icerisine role bilgisi ekliyoruz
                .claim("tokenVersion", user.getTokenVersion()) // token icerisine tokenVersion bilgisi ekliyoruz (gerekli durumlarda token gecersiz kilma icin)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // token gecerlilik suresi
                .signWith(secretKey) 
                .compact(); 
    }

    // Refresh Token üretimi
    public String generateRefreshToken(User user) {
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(jti)
                .setSubject(user.getId().toString())
                .claim("tokenVersion", user.getTokenVersion())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationInMs))
                .signWith(secretKey)
                .compact();
    }

    // Token icerisinden userId bilgisini aliyoruz
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    // Token icerisinden role bilgisini aliyoruz
    public String getUserRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Token icerisinden tokenVersion bilgisini aliyoruz
    public Integer getTokenVersionFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("tokenVersion", Integer.class);
    }

    // Token'ın içindeki tüm claim'leri döner
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

     // Token'ın geçerliliğini kontrol etme
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Token'ın versiyonu - claim tokenVersion karşılaştırması için 
    public Integer getVersionFromToken(String token) {
        Number n = parseClaims(token).get("ver", Number.class);
        return n == null ? 0 : n.intValue();
    }

    // Token'ın jti'sini döner - blacklist veya logging için kullanılacak
    public String getJtiFromToken(String token) {
        return parseClaims(token).getId();
    }

    // Token'ın kalan geçerlilik süresi(saniye) - blacklist'e TTL koymak için
    public long getRemainingTtlSeconds(String token) { 
        Date exp = parseClaims(token).getExpiration();
        long diff = exp.getTime() - System.currentTimeMillis();
        return Math.max(0L, diff / 1000L);
    }


    // Header'dan access token alma
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " kısmını kırp
        }
        return null;
    }

    // Cookie'den refresh token alma
    public String resolveRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
