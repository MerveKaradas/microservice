package com.fintech.accountservice.model;

import java.util.UUID;
import java.time.Instant;
import java.math.BigDecimal;

import javax.persistence.*;


@Table(name = "accounts")
@Entity
public class Account implements FundingSource{

    @Id
    @Column(columnDefinition = "uuid") 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID id;

    @Column(columnDefinition = "uuid", nullable = false)
    private UUID userId; // user-serviceden gelecek 

    @Column(nullable = false)    
    boolean isUserProfileCompleted;

    @Column(nullable = false,unique = true, length =16)
    private String accountNumber; // 16 haneli tekil hesap no

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currentType;

    @Column(nullable = false, precision = 19,scale=4)
    private BigDecimal availableBalance = BigDecimal.ZERO; // kullabılabilir bakiye
    @Column(nullable= false, precision= 19,scale=4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2) // faiz oranı 99.99 genellikle geçmez
    private BigDecimal interestRate = BigDecimal.ZERO;  // faiz oranı default olarak 0 yapıyoruz vadesiz hesaplarda kullanılabilir olacak
    private Instant maturityDate; // vadeli hesaplar için vade tarihi

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE; 

    @Column(nullable = false)
    private boolean isPrimary = false; // kullanıcının birincil hesap kontrolü için

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now(); 
    private Instant updatedAt;


    public Account() {}

    public Account(UUID userId, String accountNumber, AccountType accountType, Currency currentType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.currentType = currentType;
    }

   @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public Currency getCurrentType() {
        return currentType;
    }
    public void setCurrentType(Currency currentType) {
        this.currentType = currentType;
    }
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Instant getMaturityDate() {
        return maturityDate;
    }
    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
    public boolean isPrimary() {
        return isPrimary;
    }
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isUserProfileCompleted() {
        return isUserProfileCompleted;
    }

    public void setUserProfileCompleted(boolean isUserProfileCompleted) {
        this.isUserProfileCompleted = isUserProfileCompleted;
    }

    



    





}