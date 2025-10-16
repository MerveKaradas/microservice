package com.fintech.accountservice.service.concretes;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fintech.accountservice.model.base.Account;
import com.fintech.accountservice.model.entity.CreditAccount;
import com.fintech.accountservice.model.entity.CurrentAccount;
import com.fintech.accountservice.model.entity.ForeignCurrencyAccount;
import com.fintech.accountservice.model.entity.InvestmentAccount;
import com.fintech.accountservice.model.entity.SavingsAccount;
import com.fintech.accountservice.model.enums.AccountType;
import com.fintech.accountservice.model.enums.Currency;


@Service
public class AccountFactory {
    
    // TODO :Vade tarihleri ve faiz oranları hesaplara göre düzenleme yapılacak
    public Account createAccount(UUID userId,String accountNumber, AccountType accountType, Currency currency){

        Account.AccountBuilder<?,?> specificBuilder;

        switch(accountType) {
            case CURRENT : //vadesiz hesap
                specificBuilder = CurrentAccount.builder();
                break;
               
            case SAVINGS : //vadeli hesap
                specificBuilder = SavingsAccount.builder();
                break;

            case INVESTMENT : //yatırım hesabı
                specificBuilder = InvestmentAccount.builder();
                break;

            case CREDIT : //yatırım hesabı
                specificBuilder = CreditAccount.builder(); 
                break;

            case FOREIGN_CURRENCY : //yatırım hesabı
                specificBuilder = ForeignCurrencyAccount.builder();
                break;

            default :
                throw new IllegalArgumentException("Geçersiz hesap türü : " + accountType.toString());
            
        }

        return specificBuilder
                .userId(userId)
                .accountNumber(accountNumber)
                .accountType(accountType)                    
                .currency(currency)                                        
                .build();

    }
    
}
