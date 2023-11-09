package com.project.auction.service.repository;

import com.project.auction.service.entity.BidRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<BidRecord,Long> {

}
