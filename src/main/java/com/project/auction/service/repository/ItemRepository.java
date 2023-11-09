package com.project.auction.service.repository;

import com.project.auction.service.entity.ItemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
    public interface ItemRepository extends JpaRepository<ItemRecord, Long> {
        List<ItemRecord> findByAuctionSlotBetween(Timestamp startTimestamp, Timestamp endTimestamp);
    }
