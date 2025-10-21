package com.fintech.transactionservice.service.concretes;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fintech.transactionservice.model.entity.OutboxEvent;
import com.fintech.transactionservice.repository.OutboxRepository;

@Service
public class OutboxWorker {

    private static Logger log = LoggerFactory.getLogger(OutboxWorker.class);
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutboxWorker(OutboxRepository outboxRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Transactional
    @Scheduled(fixedRate = 5000) // 5saniye
    public void processOutboxEvents(){

        List<OutboxEvent> events = outboxRepository.findTop50ByProcessedFalseOrderByCreatedAtAsc();

        for(OutboxEvent event : events) {
            try{
                kafkaTemplate.send(event.getTopic(), event.getId().toString(), event.getPayload());
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch(Exception ex) {
                log.error("Kafka event gönderilemedi hata : {}", ex);
            }
        }
    }

    
    
}
