package com.project.auction.service.service;

import com.project.auction.service.entity.ItemRecord;
import com.project.auction.service.repository.AuctionRepository;
import com.project.auction.service.repository.ItemRepository;
import com.project.auction.service.service.processors.AuctionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuctionProcessorTest {


    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Autowired
    @InjectMocks
    private AuctionProcessor auctionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartAuction() {
        // Given
        LocalDate currentDate = LocalDate.now();
        Timestamp startOfDay = Timestamp.valueOf(currentDate.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(currentDate.atStartOfDay().plusDays(1));
        List<ItemRecord> todayRecords = new ArrayList<>();
        ItemRecord itemRecord = new ItemRecord(); // Set up an item record for testing
        todayRecords.add(itemRecord);
        when(itemRepository.findByAuctionSlotBetween(startOfDay, endOfDay)).thenReturn(todayRecords);

        // When
        Long result = auctionProcessor.startAuction();

        // Then
        assertEquals(result, itemRecord.getId());
    }

    @Test
    void testSendAuctionRecordsToAudit() {
        // Given
        List<ItemRecord> todayRecords = new ArrayList<>();
        ItemRecord itemRecord = new ItemRecord(); // Set up an item record for testing
        todayRecords.add(itemRecord);

        // When
        Long result = auctionProcessor.sendAuctionRecordsToAudit(todayRecords);

        // Then
        assertEquals(result, itemRecord.getId());
        assertEquals(auctionProcessor.itemBidAmountInfoMap.size(), todayRecords.size());
    }

    @Test
    void testStopAuction() {
        // Given
        Long currentActiveAuctionId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        // When
        String result = auctionProcessor.stopAuction(currentActiveAuctionId);

        // Then
        verify(auctionRepository, times(1)).makeAuctionInactive(currentActiveAuctionId);
        assertEquals(result, "Auction Made Inactive");
        assertEquals(auctionProcessor.itemBidAmountInfoMap.size(), 0);
    }
}
