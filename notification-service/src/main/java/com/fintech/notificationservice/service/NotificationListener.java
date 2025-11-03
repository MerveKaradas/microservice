package com.fintech.notificationservice.service;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.common.event.user.ResponseUserInfoDto;
import com.fintech.notificationservice.client.UserClient;

@Service
public class NotificationListener {

    private final EmailService emailService;
    private final ObjectMapper mapper;
    private final UserClient userClient;
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    
    public NotificationListener(EmailService emailService, ObjectMapper mapper, UserClient userClient) {
        this.emailService = emailService;
        this.mapper = mapper;
        this.userClient = userClient;
    }

   
    @KafkaListener(topics = "notification.transaction.deposit", groupId = "notification-service")
    public void onDepositwithEmail(String payload) {
        try {
            Map<String, Object> event = mapper.readValue(payload, new TypeReference<>() {});
            
            String userId = event.get("userId").toString();
            // String senderId = event.get("senderId").toString();
            // String receiverId = event.get("receiverId").toString();
            // String transactionType = event.get("transactionType").toString();
            String transactionId = event.get("transactionId").toString();
            String currency = event.get("currency").toString();
            String amount = event.get("amount").toString();
            String timestamp = event.get("timestamp").toString();

            ResponseEntity<ResponseUserInfoDto> userInfo = userClient.getUserInfo(UUID.fromString(userId));
            String email = userInfo.getBody().getEmail();
            String fullName = userInfo.getBody().getFirstName() + " " + userInfo.getBody().getLastName();

            if (email != null) {
                String body = """
                    Merhaba %s,
                    
                    Hesabınıza %s %s yatırıldı.
                    İşlem Tarihi: %s
                    İşlem ID: %s
                """.formatted(fullName, amount, currency, timestamp, transactionId);
                
                emailService.sendSimple(email, "Para Yatırma Bildirimi", body);
                log.info("Bildirim e-postası gönderildi -> {}", email);
            } else {
                log.warn("Kullanıcının e-posta adresi bulunamadı: {}", userId);
            }

        } catch (Exception e) {
            log.error("Deposit notification işlenirken hata oluştu:", e);
        }
    }

    @KafkaListener(topics = "notification.transaction.withdraw", groupId = "notification-service")
    public void onWithdrawWithEmail(String payload) {
        try {
            Map<String, Object> event = mapper.readValue(payload, new TypeReference<>() {});
            
            String userId = event.get("userId").toString();
            String transactionId = event.get("transactionId").toString();
            String currency = event.get("currency").toString();
            String amount = event.get("amount").toString();
            String timestamp = event.get("timestamp").toString();

            ResponseEntity<ResponseUserInfoDto> userInfo = userClient.getUserInfo(UUID.fromString(userId));
            String email = userInfo.getBody().getEmail();
            String fullName = userInfo.getBody().getFirstName() + " " + userInfo.getBody().getLastName();

            if (email != null) {
                String body = """
                    Merhaba %s,
                    
                    Hesabınızdan %s %s çekildi.
                    İşlem Tarihi: %s
                    İşlem ID: %s
                """.formatted(fullName, amount, currency, timestamp, transactionId);
                
                emailService.sendSimple(email, "Para Çekme Bildirimi", body);
                log.info("Bildirim e-postası gönderildi -> {}", email);
            } else {
                log.warn("Kullanıcının e-posta adresi bulunamadı: {}", userId);
            }

        } catch (Exception e) {
            log.error("Withdraw notification işlenirken hata oluştu:", e);
        }
    }

     @KafkaListener(topics = "notification.transaction.transfer", groupId = "notification-service")
    public void onTransferWithEmail(String payload) {
        try {
            Map<String, Object> event = mapper.readValue(payload, new TypeReference<>() {});
            
            String userId = event.get("userId").toString();
            String transactionId = event.get("transactionId").toString();
            String currency = event.get("currency").toString();
            String amount = event.get("amount").toString();
            String timestamp = event.get("timestamp").toString();

            ResponseEntity<ResponseUserInfoDto> userInfo = userClient.getUserInfo(UUID.fromString(userId));
            String email = userInfo.getBody().getEmail();
            String fullName = userInfo.getBody().getFirstName() + " " + userInfo.getBody().getLastName();

            if (email != null) {
                String body = """
                    Merhaba %s,
                    
                    Hesabınızdan %s %s transfer işlemi gerçekleştirildi.
                    İşlem Tarihi: %s
                    İşlem ID: %s
                """.formatted(fullName, amount, currency, timestamp, transactionId);
                
                emailService.sendSimple(email, "Para Transfer Bildirimi", body);
                log.info("Bildirim e-postası gönderildi -> {}", email);
            } else {
                log.warn("Kullanıcının e-posta adresi bulunamadı: {}", userId);
            }

        } catch (Exception e) {
            log.error("Transfer notification işlenirken hata oluştu:", e);
        }
    }
}
