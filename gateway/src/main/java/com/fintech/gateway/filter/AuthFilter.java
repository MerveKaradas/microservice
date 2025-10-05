package com.fintech.gateway.filter;

import com.fintech.gateway.dto.TokenResponseDto;
import com.fintech.gateway.dto.UserTokenStatusResponseDto;
import com.fintech.gateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class AuthFilter implements GlobalFilter {

    private final WebClient webClient;
    private final JwtUtil jwtUtil;

    // Açık (permitAll) endpointler
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/signUp",
            "/api/auth/refresh",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs"
    );

    public AuthFilter(WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.webClient = webClientBuilder.baseUrl("http://auth-service:8080").build();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Public endpointler için akışa devam
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Protected endpointler için Authorization kontrol
        String accessToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        accessToken = accessToken.substring(7); 

        try {
             
            Claims claims = jwtUtil.extractAllClaims(accessToken);
            UUID userId = UUID.fromString(claims.getSubject());
            Integer tokenVersionFromToken = claims.get("tokenVersion", Integer.class);
           
            if (userId == null || tokenVersionFromToken == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // Güncel tokenVersion bilgiisini auth-serviceden alıyoruz
            return webClient.get()
                    .uri("/api/auth/" + userId + "/token-status")
                    .retrieve()
                    .bodyToMono(UserTokenStatusResponseDto.class)
                    .flatMap(status -> {
                        if (status.isDeleted() || !tokenVersionFromToken.equals(status.getTokenVersion())) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    });


        } catch (ExpiredJwtException e) {
            // Access token süresi dolmuşsa
            String refreshToken = getRefreshTokenFromCookie(exchange);
            if (refreshToken == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // AuthService üzerinden refresh token ile yenileme
            return webClient.post()
                    .uri("/api/auth/refresh")
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken)
                    .retrieve()
                    .bodyToMono(TokenResponseDto.class)
                    .flatMap(tokens -> {
                        // Yeni access token header'a eklenir
                        exchange.getRequest().mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                                .build();
                        // Yeni refresh token cookie olarak eklenir
                        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE,
                                "refreshToken=" + tokens.getRefreshToken() + "; HttpOnly; Path=/; SameSite=Strict; Secure");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(ex -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }
    }

    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private String getRefreshTokenFromCookie(ServerWebExchange exchange) {
        if (exchange.getRequest().getCookies().getFirst("refreshToken") != null) {
            return exchange.getRequest().getCookies().getFirst("refreshToken").getValue();
        }
        return null;
    }
}