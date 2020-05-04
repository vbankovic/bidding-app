package com.homework.biddingapp.service;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.OfferStatus;
import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.repository.OfferRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfferService {

  @Autowired private OfferRepository offerRepository;

  @Transactional
  public Offer create(OfferDto offerDto) {
    return offerRepository.save(createOfferFromDTO(offerDto));
  }

  public void accept(Offer offer) {
    offer.setStatus(OfferStatus.ACCEPTED);
  }

  public void reject(Offer offer) {
    if (isAccepted(offer)) {
      throw new IllegalStateException(
          String.format(
              "Offer with id: %d is already accepted, so it cannot be rejected.", offer.getId()));
    }
    offer.setStatus(OfferStatus.REJECTED);
  }

  public boolean isAccepted(Offer offer) {
    return OfferStatus.ACCEPTED.equals(offer.getStatus());
  }

  public Set<Offer> getByBidderId(long bidderId) {
    if (offerRepository.findByBidderId(bidderId).isPresent()) {
      return new HashSet<>(offerRepository.findByBidderId(bidderId).get());
    }
    return new HashSet<>();
  }

  public Set<OfferDto> getOffersByBidderId(long bidderId) {
    return getByBidderId(bidderId).stream() //
        .map(OfferDto::new) //
        .collect(Collectors.toSet()); //
  }

  private Offer createOfferFromDTO(OfferDto dto) {
    return new Offer(
        dto.getBidderId(), //
        dto.getDescription(), //
        new BigDecimal(dto.getPrice()), //
        Currency.valueOf(dto.getCurrency())); //
  }
}
