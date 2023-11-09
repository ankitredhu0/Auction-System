package com.project.auction.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auction.service.model.BidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BidConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BidConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BidProcessor bidProcessor;

    @KafkaListener(topics = "${spring.kafka.bid.ingestor.topic}" ,containerFactory = "bidListenerContainerFactory")
    public void consumeBidRequest(@Payload String message) {

        try {
            BidRequest bidRequest = objectMapper.readValue(message, BidRequest.class);
            logger.info("Received new Bid : {}", bidRequest);
            bidProcessor.process(bidRequest);

        } catch (JsonProcessingException ex) {
            logger.error("Error in consuming bid message: {}", ex.getLocalizedMessage());
        }
    }
}
