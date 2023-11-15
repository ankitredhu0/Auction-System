package com.project.auction.service.service.processors;

import com.project.auction.service.model.AuctionAuditMessage;
import com.project.auction.service.model.AuctionState;
import com.project.auction.service.model.BidAuditMessage;
import com.project.auction.service.model.BidRequest;
import com.project.auction.service.service.producers.AuctionAuditProducer;
import com.project.auction.service.service.producers.BidAuditProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BidProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BidProcessor.class);

    @Autowired
    private AuctionProcessor auctionProcessor;

    @Autowired
    private AuctionAuditProducer auditProducer;

    @Autowired
    private BidAuditProducer bidAuditProducer;

    public void process(BidRequest bidRequest){

        try{
            AuctionAuditMessage currentItem = auctionProcessor.itemBidAmountInfoMap.get(bidRequest.getItemId());

            if(currentItem != null){

                if(currentItem.getWinningAmount() == 0L){
                    currentItem.setWinningAmount(currentItem.getMinimumPrice());
                }

                if(currentItem.getAuctionState().equals(AuctionState.ACTIVE) &&
                        bidRequest.getBiddingPrice() > currentItem.getWinningAmount()){

                        //updating in memory first
                        currentItem.setWinningAmount(bidRequest.getBiddingPrice());

                        AuctionAuditMessage auctionAuditMessage = AuctionAuditMessage.builder()
                                .itemId(bidRequest.getItemId())
                                .winningAmount(bidRequest.getBiddingPrice())
                                .winner(bidRequest.getUserId())
                                .build();

                        auditProducer.sendMessage(auctionAuditMessage);
                        logger.info("Bid details Updated in auction_pipeline table.. ");

                    BidAuditMessage bidAuditMessage = BidAuditMessage.builder()
                            .biddingPrice(bidRequest.getBiddingPrice())
                            .id(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)
                            .userId(bidRequest.getUserId())
                            .itemId(bidRequest.getItemId())
                            .build();

                    bidAuditProducer.sendMessage(bidAuditMessage);
                    logger.info("Bid Dumped in Bid table : {} ",bidAuditMessage);

                    }
                    else{
                        logger.error("ERROR ", new Throwable("Low Bidding Amount, Please Increase it"));
                    }
            }
            else{
                logger.info("ERROR ", new Throwable("Item is not live for Auction"));
            }

        }catch (Exception ex){
            logger.error("Error in Bid processing : {}", ex.getLocalizedMessage());
        }
    }
}
