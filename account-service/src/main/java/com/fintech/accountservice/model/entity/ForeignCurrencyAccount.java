package com.fintech.accountservice.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fintech.accountservice.model.base.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "foreignCurrencyAccounts")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ForeignCurrencyAccount extends Account { // Döviz hesabı

    
}
