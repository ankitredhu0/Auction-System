package com.project.auction.service.controller;

import com.project.auction.service.service.AuctionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RequestMapping("/api")
@RestController
public class AuctionController {

    @Autowired
    private AuctionProcessor auctionProcessor;

    @PostMapping("/start-auction")
    public ResponseEntity<Long> startAuction() {

        Long auctionResponse = auctionProcessor.startAuction();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(auctionResponse);
    }

    @PostMapping("/stop-auction/{id}")
    public ResponseEntity<String> stopAuction(@PathVariable Long id) {

        String auctionResponse = auctionProcessor.stopAuction(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(auctionResponse);
    }
}
