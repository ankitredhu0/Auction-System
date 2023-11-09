package com.project.auction.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "item_bucket")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRecord {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "item_category")
    private String itemCategory;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "auction_slot")
    private Timestamp auctionSlot;

    @Column(name = "base_price")
    private Long basePrice;

    @Column(name = "condition")
    private String condition;

    @Column(name = "buying_year")
    private String buyingYear;

}
