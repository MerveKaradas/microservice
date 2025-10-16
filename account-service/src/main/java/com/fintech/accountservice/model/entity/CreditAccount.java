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
@Table(name = "creditAccounts")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreditAccount extends Account{

    @Builder.Default
    @Column(nullable =false, precision = 19, scale = 4) // faiz oranı 99.99 genellikle geçmez
    private BigDecimal creditLimit = new BigDecimal("100.000");  // default olarak şuan değer atadık
    
    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal apr = new BigDecimal("0.18"); // Yıllık faiz oranı (APR)

    @Builder.Default
    @Column(nullable = false)
    private Instant nextPaymentDueDate = LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC); // vadeli hesaplar için vade tarihi
    // todo : ileride bu kısımlar düzeltilecek
    
}
