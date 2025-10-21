package com.fintech.accountservice.aspect;

import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.fintech.accountservice.repository.UserFlagsRepository;
import com.fintech.accountservice.security.JwtUtil;

@Aspect
@Component
public class ProfileCompletionAspect {

    private final JwtUtil jwtUtil;
    private final UserFlagsRepository userFlagsRepository;

    public ProfileCompletionAspect(JwtUtil jwtUtil, UserFlagsRepository userFlagsRepository) {
        this.jwtUtil = jwtUtil;
        this.userFlagsRepository = userFlagsRepository;
    }

    @Before("@annotation(RequireProfileComplete) && args(token,..)")
    public void checkProfileComplete(String token) {
       
        boolean profileCompleted = userFlagsRepository.existsByUserIdAndProfileCompleteTrue(jwtUtil.extractUserId(token));

        if(!profileCompleted){
            throw new IllegalStateException("Kullanıcı profil bilgileri tamamlanmadan önce mevcut işleme devam edilemez.");
        }


    }
    
}
