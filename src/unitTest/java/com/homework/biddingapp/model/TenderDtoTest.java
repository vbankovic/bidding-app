package com.homework.biddingapp.model;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.Tender;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TenderDtoTest {

  @Test
  public void constructionFromTender() {
    Offer offer1 = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);
    offer1.setId(1);
    Set<OfferDto> offerDtoSet = new HashSet<>();
    offerDtoSet.add(new OfferDto(offer1));

    Tender tender = new Tender(1l, 1l, "this is work description");
    tender.getOffers().add(offer1);

    TenderDto tenderDto = new TenderDto(tender);

    assertEquals(tender.getId(), tenderDto.getId());
    assertEquals(tender.getProjectId(), tenderDto.getProjectId());
    assertEquals(tender.getIssuerId(), tenderDto.getIssuerId());
    assertEquals(tender.getWorkDescription(), tenderDto.getWorkDescription());

    assertTrue(offerDtoSet.size() == tenderDto.getOffers().size());

    List<OfferDto> expected = new ArrayList<>(offerDtoSet);
    List<OfferDto> actual = new ArrayList<>(tenderDto.getOffers());

    assertEquals(expected.get(0).getId(), actual.get(0).getId());
    assertEquals(expected.get(0).getBidderId(), actual.get(0).getBidderId());
    assertEquals(expected.get(0).getPrice(), actual.get(0).getPrice());
    assertEquals(expected.get(0).getCurrency(), actual.get(0).getCurrency());
    assertEquals(expected.get(0).getStatus(), actual.get(0).getStatus());

  }
}