package com.project.auction.service.repository;

import com.project.auction.service.entity.AuditDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AuctionRepository extends JpaRepository<AuditDto,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE auction_pipeline SET auction_state = 'INACTIVE' WHERE auction_id = ?1",
            nativeQuery = true)
    void makeAuctionInactive(@Param("id") Long id);
}
