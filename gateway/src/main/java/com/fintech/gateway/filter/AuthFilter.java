package com.fintech.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders; 
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.fintech.gateway.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import com.fintech.gateway.dto.TokenResponseDto;
import org.springframework.http.HttpStatus;

@Component
public class AuthFilter implements GlobalFilter {

    private final WebClient webClient;
    private final JwtUtil JwtUtil;
    
    public AuthFilter(WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.webClient = webClientBuilder.baseUrl("http://auth-service:8080").build();
        this.JwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (accessToken == null) {
            return chain.filter(exchange); // Public endpointler için akışa izin veriyoruz
        }

        try {
            // JWT doğrulama
            JwtUtil.validateAccessToken(accessToken); 
            return chain.filter(exchange);
        } catch (ExpiredJwtException e) { // access token süresi dolunca
            
            // refresh token kontrolü
            String refreshToken = getRefreshTokenFromCookie(exchange);
            if (refreshToken == null) { // refresh token yoksa unauthorized
                return unauthorized(exchange);
            }

            // Eğer refresh token varsa Auth-service üzerinden yeni token alıyoruz
            return webClient.post()
                    .uri("/auth/refresh")
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken)
                    .retrieve()
                    .bodyToMono(TokenResponseDto.class)
                    .flatMap(tokens -> {
                        // Yeni access tokenı headera ekleme
                        exchange.getRequest().mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                                .build();
                        // Responsea refresh token cookie ekleme
                        exchange.getResponse().getHeaders().add(HttpHeaders.SET_COOKIE,
                                "refreshToken=" + tokens.getRefreshToken() + "; HttpOnly; Path=/");
                        return chain.filter(exchange);
                    })
                    .onErrorResume(ex -> unauthorized(exchange)); // Token yenileme başarısız olursa unauthorized
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private String getRefreshTokenFromCookie(ServerWebExchange exchange) {
        return exchange.getRequest().getCookies()
                .getFirst("refreshToken") != null ?
                exchange.getRequest().getCookies().getFirst("refreshToken").getValue() : null;
    }

    
}
