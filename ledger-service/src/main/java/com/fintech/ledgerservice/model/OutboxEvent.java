package com.fintech.ledgerservice.model;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "outboxs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name="UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name="id", updatable = false , nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String topic;
    
    @Lob
    @Column(nullable = false)
    private String payload;

    @Builder.Default
    @Column(nullable = false)
    private boolean processed = false;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();


    
}
