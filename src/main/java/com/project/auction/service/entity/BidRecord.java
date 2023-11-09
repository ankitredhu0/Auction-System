package com.project.auction.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "bid_dump")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BidRecord {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "bidding_price")
    private Long biddingPrice;

    @Column(name = "user_id")
    private Long userId;

}
