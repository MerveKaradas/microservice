package com.fintech.authservice.service.concretes;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fintech.common.event.auth.AuthEvent;
import com.fintech.common.event.auth.EventData;

import org.springframework.kafka.core.KafkaTemplate;

@Component
public class EventListener {

    private final KafkaTemplate<String, AuthEvent<?>> kafkaTemplate;

    public EventListener(KafkaTemplate<String, AuthEvent<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Bu anatasyon Spring’in event dinleme mekanizması ile çalışan metotlar için geçerlidir. (ApplicationEventPublisher)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // Transaction tamamlandıktan sonra tetikleniyor(Rollback için)
    public void publish(AuthEvent<? extends EventData> event) {
        kafkaTemplate.send("auth-events", event.getEventId(), event);
    }
    
    
}
