package com.project.auction.service.service;

import com.project.auction.service.entity.BidRecord;
import com.project.auction.service.model.AuctionAuditMessage;
import com.project.auction.service.model.AuctionState;
import com.project.auction.service.model.BidRequest;
import com.project.auction.service.repository.BidRepository;
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
    private AuditProducer auditProducer;

    @Autowired
    private BidRepository bidRepository;

    public void process(BidRequest bidRequest){

        try{
            AuctionAuditMessage currentItem = auctionProcessor.itemBidAmountInfoMap.get(bidRequest.getItemId());

            if(currentItem != null){

                if(currentItem.getWinningAmount() == 0L){
                    currentItem.setWinningAmount(currentItem.getMinimumPrice());
                }

                if(currentItem.getAuctionState().equals(AuctionState.ACTIVE) &&
                        bidRequest.getBiddingPrice() > currentItem.getWinningAmount()){

                        //updating inmemory first
                        currentItem.setWinningAmount(bidRequest.getBiddingPrice());

                        AuctionAuditMessage auctionAuditMessage = AuctionAuditMessage.builder()
                                .itemId(bidRequest.getItemId())
                                .winningAmount(bidRequest.getBiddingPrice())
                                .winner(bidRequest.getUserId())
                                .build();

                        auditProducer.sendMessage(auctionAuditMessage);
                        logger.info("Bid details sent in auction_pipeline table.. ");


                        dumpInBidTable(bidRequest);


                    }
                    else{
                        logger.error("ERRRORRR ", new Throwable("Low Bidding Amount, Please Increase it"));
                    }
            }
            else{
                logger.info("ERRRORRR ", new Throwable("Item is not live for Auction"));
            }

        }catch (Exception ex){
            logger.error("Error in Bid processing ", ex.getLocalizedMessage());
        }
    }

    public void dumpInBidTable(BidRequest bidRequest){
        try {
            BidRecord bidRecord = BidRecord.builder()
                    .userId(bidRequest.getUserId())
                    .biddingPrice(bidRequest.getBiddingPrice())
                    .id(UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE)
                    .itemId(bidRequest.getItemId())
                    .build();
            bidRepository.save(bidRecord);
            logger.info("SuccessFully saved this bid in table",bidRecord);
        }catch(Exception e){
            logger.error("Error in saving Bid into bid dump table");
        }
    }
}
