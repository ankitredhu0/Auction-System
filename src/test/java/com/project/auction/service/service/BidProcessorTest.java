package com.project.auction.service.service;

import com.project.auction.service.entity.BidRecord;
import com.project.auction.service.model.AuctionAuditMessage;
import com.project.auction.service.model.BidRequest;
import com.project.auction.service.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BidProcessorTest {

    @Mock
    private AuctionProcessor auctionProcessor;

    @Mock
    private BidRepository bidRepository;

    @Autowired
    @InjectMocks
    private BidProcessor bidProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize itemBidAmountInfoMap in auctionProcessor
        auctionProcessor.itemBidAmountInfoMap = new HashMap<>();
    }

    @Test
    void testProcessLowBidAmount() {
        // Given
        BidRequest bidRequest = new BidRequest();
        bidRequest.setItemId(UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE);
        bidRequest.setBiddingPrice(50L);
        bidRequest.setUserId(284724L);

        AuctionAuditMessage auctionAuditMessage = AuctionAuditMessage.builder()
                .itemId(bidRequest.getItemId())
                .winningAmount(100L)
                .winner(283472L)
                .build();

        auctionProcessor.itemBidAmountInfoMap.put(bidRequest.getItemId(), auctionAuditMessage);

        // When
        bidProcessor.process(bidRequest);

        // Then
        verify(bidRepository, times(0)).save(any(BidRecord.class));
        // Ensure that an error is logged about low bidding amount
    }

}
