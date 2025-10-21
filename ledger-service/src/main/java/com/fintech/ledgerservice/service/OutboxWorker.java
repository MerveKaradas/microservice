package com.fintech.ledgerservice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fintech.ledgerservice.model.OutboxEvent;
import com.fintech.ledgerservice.repository.OutboxRepository;

@Service
public class OutboxWorker {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private static Logger log = LoggerFactory.getLogger(OutboxWorker.class);

    public OutboxWorker(KafkaTemplate<String, Object> kafkaTemplate, OutboxRepository outboxRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void processOutboxEvents(){

        List<OutboxEvent> events = outboxRepository.findTop50ByProcessedFalseOrderByCreatedAtAsc();

        for(OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(),event.getId().toString(),event.getPayload());
                event.setProcessed(true);
                outboxRepository.save(event);
            }catch(Exception ex) {
                log.error("Kafka event gönderilemedi hata : {}", ex);
            }
        }
        
    }
    
}
