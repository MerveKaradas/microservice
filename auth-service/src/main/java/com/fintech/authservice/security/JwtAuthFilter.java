package com.fintech.authservice.security;

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fintech.authservice.model.User;
import com.fintech.authservice.repository.UserRepository;
import com.fintech.authservice.service.concretes.JwtBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JwtBlacklistService blacklistService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userRepository, JwtBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
 
                String authHeader = request.getHeader("Authorization");

                // Koşul : Eğer authHeader null ise veya "Bearer " ile başlamıyorsa, filtre zincirine devam et
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }    

                String token = authHeader.substring(7);

                //TOKEN DOĞRULAMA;
                Claims claims;
                try {
                    claims = jwtUtil.parseClaims(token);
                    
                } catch (ExpiredJwtException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token süresi dolmuş");
                    return;
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Geçersiz token");
                    return;
                }

                // BLACKLIST KONTROLÜ

                String jti = claims.getId();
                
                if (blacklistService.isBlacklisted(jti)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token iptal edilmiş");
                    return;
                }

                Long userId = Long.parseLong(claims.getSubject());
                Integer tokenVersion = claims.get("tokenVersion",Integer.class);

                if(tokenVersion == null){
                    tokenVersion = 0;
                }

                // AKTIF KULLANICILAR

                User user;
                try {
                    user = userRepository.findByIdAndDeletedAtIsNull(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı")); // Soft delete kontrolü 
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Kullanıcı bulunamadı veya silinmiş.");
                    return;
                }

                // TOKEN VERSIYON KONTROLU
                if(!Objects.equals(tokenVersion, user.getTokenVersion())){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token geçersiz kılınmış");
                    return;
                }

                // SecurityContext 
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
    }
    
}
