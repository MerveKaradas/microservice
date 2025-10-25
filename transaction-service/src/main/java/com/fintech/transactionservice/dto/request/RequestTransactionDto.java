package com.fintech.transactionservice.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RequestTransactionDto {

    @NotBlank(message = "İşlem türü boş olamaz!")
    @Pattern(regexp = "DEPOSIT|WITHDRAW|TRANSFER")
    private String transactionType;

    @NotNull(message = "Hesap ID'si boş olamaz!")
    private UUID accountId;
    private UUID targetId;

    @NotBlank(message = "Para birimi boş olamaz!")
    @Pattern(regexp = "TRY|USD|EUR")
    private String currency;

    @NotNull(message = "Para miktarı boş olamaz!")
    @DecimalMin(value = "0", inclusive = false, message = "Para miktarı 0'dan büyük olmalıdır!")
    private BigDecimal amount;

    
    public RequestTransactionDto() {
    }

    

    public RequestTransactionDto(
            String transactionType,
            UUID accountId, UUID targetId,
            String currency,
            BigDecimal amount) {
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.targetId = targetId;
        this.currency = currency;
        this.amount = amount;
    }



    public String getTransactionType() {
        return transactionType;
    }


    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }


    public UUID getAccountId() {
        return accountId;
    }


    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }


    public UUID getTargetId() {
        return targetId;
    }


    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }


    public String getCurrency() {
        return currency;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    

    
    
                                      
    
}
