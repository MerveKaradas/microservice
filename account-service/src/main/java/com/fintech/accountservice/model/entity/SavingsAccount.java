package com.fintech.accountservice.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fintech.accountservice.model.base.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "savingsAccounts")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SavingsAccount extends Account{ //vadeli hesap

    @Builder.Default
    @Column(precision = 5, scale = 2) // faiz oranı 99.99 genellikle geçmez
    private BigDecimal interestRate = new BigDecimal("0.005");  // faiz oranı şimdilik ör değer verdik hardcode olarak
    // TODO : Spring Cloud Config Server kurulup içerisinden çekilecek
    
    @Builder.Default
    private Instant maturityDate = LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.UTC); // vadeli hesaplar için vade tarihi

 
     
}
