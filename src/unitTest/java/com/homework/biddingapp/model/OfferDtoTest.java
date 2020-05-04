package com.homework.biddingapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class OfferDtoTest {

  @Test
  public void constructionFromOffer() {
    Offer offer = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);

    OfferDto offerDto = new OfferDto(offer);

    assertEquals(offer.getId(), offerDto.getId());
    assertEquals(offer.getBidderId(), offerDto.getBidderId());
    assertEquals(offer.getPrice().toString(), offerDto.getPrice());
    assertEquals(String.valueOf(offer.getCurrency()), offerDto.getCurrency());
    assertEquals(String.valueOf(offer.getStatus()), offerDto.getStatus());
  }
}
