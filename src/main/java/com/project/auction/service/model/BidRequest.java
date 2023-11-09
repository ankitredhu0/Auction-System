package com.project.auction.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidRequest {

    private Long itemId;
    private Long biddingPrice;
    private Long userId;

}
