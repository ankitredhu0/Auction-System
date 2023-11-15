package com.project.auction.service.service.producers;

import com.project.auction.service.model.AuctionAuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuctionAuditProducer {

    private static final Logger logger = LoggerFactory.getLogger(AuctionAuditProducer.class);

    @Value("${spring.kafka.auction.audit.topic}")
    private String auctionAuditTopic;

    private final KafkaTemplate<String, AuctionAuditMessage> kafkaTemplate;

    @Autowired
    AuctionAuditProducer(KafkaTemplate<String, AuctionAuditMessage> auditProducerKafkaTemplate) {
        this.kafkaTemplate = auditProducerKafkaTemplate;
    }

    public void sendMessage(AuctionAuditMessage auditMessage) {
        try {
            kafkaTemplate.send(auctionAuditTopic, auditMessage);
            logger.info("AuctionAuditProducer : Published message : {}", auditMessage);
        }
        catch (Exception e) {
            logger.error("AuctionAuditProducer : Error publishing message : {}", e);
        }
    }
}
