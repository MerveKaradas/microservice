package com.fintech.accountservice.model.base;

import java.util.UUID;
import java.time.Instant;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fintech.accountservice.model.enums.AccountStatus;
import com.fintech.accountservice.model.enums.AccountType;
import com.fintech.common.event.account.enums.Currency;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "accounts")
@Getter
@Setter
@SuperBuilder // Kalıtımlı sınıflarda builder kullanmak için
@NoArgsConstructor
public abstract class Account implements FundingSource{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "uuid", nullable = false)
    private UUID userId; // user-serviceden gelecek 

    @Column(nullable = false,unique = true, length =16)
    private String accountNumber; // 16 haneli tekil hesap no

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;


    @Column(nullable = false, precision = 19,scale=4)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO; // kullabılabilir bakiye

    @Column(nullable= false, precision= 19,scale=4)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE; 

    @Column(nullable = false)
    @Builder.Default
    private boolean isPrimary = false; // kullanıcının birincil hesap kontrolü için

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now(); 
    @Builder.Default
    private Instant updatedAt = null;
  
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
    
}