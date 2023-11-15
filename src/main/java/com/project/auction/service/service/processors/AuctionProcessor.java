package com.project.auction.service.service.processors;

import com.project.auction.service.entity.ItemRecord;
import com.project.auction.service.model.AuctionAuditMessage;
import com.project.auction.service.model.AuctionState;
import com.project.auction.service.repository.AuctionRepository;
import com.project.auction.service.repository.ItemRepository;
import com.project.auction.service.service.producers.AuctionAuditProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class AuctionProcessor {

    @Autowired
    private AuctionAuditProducer auditProducer;

    private static final Logger logger = LoggerFactory.getLogger(AuctionProcessor.class);
    public HashMap<Long, AuctionAuditMessage> itemBidAmountInfoMap = new HashMap<>();
    private final ItemRepository itemRepository;
    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionProcessor(ItemRepository itemRepository, AuctionRepository auctionRepository) {
        this.itemRepository = itemRepository;
        this.auctionRepository = auctionRepository;
    }

   // @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public Long startAuction() {
        try {
            LocalDate currentDate = LocalDate.now();
            Timestamp startOfDay = Timestamp.valueOf(currentDate.atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(currentDate.atStartOfDay().plusDays(1));

            List<ItemRecord> todayRecords = itemRepository.findByAuctionSlotBetween(startOfDay, endOfDay);
            logger.info("Successfully fetched records for todays auction");

            return sendAuctionRecordsToAudit(todayRecords);
        }catch (Exception ex){
            logger.error("Error in fetching records for today's Auction : {} ", ex.getLocalizedMessage());
            return null;
        }
    }

    public Long sendAuctionRecordsToAudit(List<ItemRecord> todayRecords){
        try {

            long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

            for (ItemRecord record : todayRecords) {
                AuctionAuditMessage auctionAuditMessage = AuctionAuditMessage.builder()
                        .auctionId(id)
                        .auctionSlot(record.getAuctionSlot())
                        .auctionState(AuctionState.ACTIVE)
                        .basePrice(record.getBasePrice())
                        .buyingYear(record.getBuyingYear())
                        .itemId(record.getId())
                        .condition(record.getCondition())
                        .description(record.getDescription())
                        .itemName(record.getItemName())
                        .itemCategory(record.getItemCategory())
                        .mediaUrl(record.getMediaUrl())
                        .winningAmount(0L)
                        .minimumPrice(record.getBasePrice()).build();

                itemBidAmountInfoMap.put(auctionAuditMessage.getItemId(),auctionAuditMessage);

                auditProducer.sendMessage(auctionAuditMessage);
                logger.info("Sending Message to kafka");
            }
            return id;
        }catch (Exception ex){
            logger.error("Error in sending Records in kafka : {}", ex.getLocalizedMessage());
            return null;
        }
    }

    //@Scheduled(cron = "59 59 23 * * ?")
    public String stopAuction(Long currentActiveAuctionId){

        try {
            logger.info("Making this Auction Inactive : {} ", currentActiveAuctionId);
            auctionRepository.makeAuctionInactive(currentActiveAuctionId);
            logger.info("clearing out saved In memory Items from Map");
            itemBidAmountInfoMap.clear();
            logger.info("Made this Auction Inactive : {} ", currentActiveAuctionId);
            return "Auction Made Inactive";
        }
        catch(Exception ex){
            logger.error("Error in Making Auction Inactive : {}", ex.getLocalizedMessage());
            return "Error";
        }

    }
}
