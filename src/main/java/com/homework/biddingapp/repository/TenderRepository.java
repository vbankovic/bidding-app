package com.homework.biddingapp.repository;

import com.homework.biddingapp.entity.Tender;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenderRepository extends JpaRepository<Tender, Long> {

  List<Tender> findByIssuerId(Long issuerId);
}
