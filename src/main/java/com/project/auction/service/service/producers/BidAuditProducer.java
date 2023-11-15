package com.project.auction.service.service.producers;

import com.project.auction.service.model.BidAuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BidAuditProducer {

    private static final Logger logger = LoggerFactory.getLogger(BidAuditProducer.class);

    @Value("${spring.kafka.bid.audit.topic}")
    private String bidAuditTopic;

    private final KafkaTemplate<String, BidAuditMessage> kafkaTemplate;

    @Autowired
    BidAuditProducer(KafkaTemplate<String, BidAuditMessage> bidAuditProducerKafkaTemplate) {
        this.kafkaTemplate = bidAuditProducerKafkaTemplate;
    }

    public void sendMessage(BidAuditMessage bidAuditMessage) {
        try {
            kafkaTemplate.send(bidAuditTopic, bidAuditMessage);
            logger.info("AuctionAuditProducer : Published message : {}", bidAuditMessage);
        }
        catch (Exception e) {
            logger.error("AuctionAuditProducer : Error publishing message : {}", e);
        }
    }
}
