package com.fintech.accountservice.service.concretes;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fintech.accountservice.model.entity.OutboxEvent;
import com.fintech.accountservice.repository.OutboxRepository;

@Service
public class OutboxWorker {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(OutboxWorker.class);

    public OutboxWorker(OutboxRepository outboxRepository,
                        KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Scheduled(fixedRate = 5000) // her 5 saniyede bir çalışır
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxRepository.findTop50ByProcessedFalseOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getId().toString() , event.getPayload());
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                // TODO : İleride retry kurulabilir
                log.error("Kafka event gönderimi başarısız: {}", e.getMessage());
            }
        }
    }
}
