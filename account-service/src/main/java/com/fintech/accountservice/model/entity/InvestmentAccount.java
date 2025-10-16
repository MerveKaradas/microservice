package com.fintech.accountservice.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fintech.accountservice.model.base.Account;
import com.fintech.accountservice.model.enums.RiskProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "investmentAccounts")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvestmentAccount extends Account { // Yatırım hesabı

    @Column(name = "portfolioId", updatable = false, nullable = false, columnDefinition = "uuid")
    @Builder.Default
    private UUID portfolioId = UUID.randomUUID();
    //TODO : İleride varlık fonları için oluşturulacak mikroservise bağlanıcak

    @Column(nullable = false)
    @Builder.Default
    private RiskProfile riskProfile = RiskProfile.MEDIUM; 
}
