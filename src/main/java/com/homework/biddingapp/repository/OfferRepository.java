package com.homework.biddingapp.repository;

import com.homework.biddingapp.entity.Offer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {

  Optional<List<Offer>> findByBidderId(long bidderId);
}
