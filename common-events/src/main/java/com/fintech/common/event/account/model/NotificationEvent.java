package com.fintech.common.event.account.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fintech.common.event.account.enums.Currency;
import com.fintech.common.event.transaction.enums.TransactionType;

import lombok.Builder;

@Builder
public class NotificationEvent {

    private UUID transactionId;
    private UUID senderId;
    private UUID receiverId;
    private UUID userId;
    private Currency currency;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Instant timestamp;

    public NotificationEvent() {
    }
    

    public NotificationEvent(UUID transactionId, UUID senderId, UUID receiverId, UUID userId, Currency currency,
            TransactionType transactionType, BigDecimal amount, Instant timestamp) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.userId = userId;
        this.currency = currency;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }


    public UUID getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
   
    public Currency getCurrency() {
        return currency;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
 
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public UUID getSenderId() {
        return senderId;
    }
    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }




    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    } 

    
    

    

    
    
}
