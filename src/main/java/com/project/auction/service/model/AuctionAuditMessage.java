package com.project.auction.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuctionAuditMessage {

    private Long itemId;
    private String itemCategory;
    private String itemName;
    private String description;
    private String mediaUrl;
    private Timestamp auctionSlot;
    private Long basePrice;
    private String condition;
    private String buyingYear;
    private AuctionState auctionState;
    private Long auctionId;
    private Long minimumPrice;
    private Long winningAmount;
    private Long winner;

}
