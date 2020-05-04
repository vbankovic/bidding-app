package com.homework.biddingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.OfferStatus;
import com.homework.biddingapp.entity.Tender;
import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.model.TenderDto;
import com.homework.biddingapp.repository.OfferRepository;
import com.homework.biddingapp.repository.TenderRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TenderServiceTest {

  @InjectMocks private TenderService tenderService;

  @Mock private OfferService offerService;

  @Mock private OfferRepository offerRepository;

  @Mock private TenderRepository tenderRepository;

  @Test
  public void createTender() {
    Tender tender = new Tender(1, 2, "this is work description");

    TenderDto tenderDto = mock(TenderDto.class);
    when(tenderDto.getProjectId()).thenReturn(tender.getProjectId());
    when(tenderDto.getIssuerId()).thenReturn(tender.getIssuerId());
    when(tenderDto.getWorkDescription()).thenReturn(tender.getWorkDescription());

    when(tenderRepository.save(any())).thenReturn(tender);
    TenderDto createdTenderDto = tenderService.create(tenderDto);

    assertEquals(tender.getProjectId(), createdTenderDto.getProjectId());
    assertEquals(tender.getIssuerId(), createdTenderDto.getIssuerId());
    assertEquals(tender.getWorkDescription(), createdTenderDto.getWorkDescription());
  }

  @Test
  public void addOffer() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    OfferDto offerDto = new OfferDto(offer);

    Tender tender = new Tender(1, 2, "this is work description");

    when(offerService.create(offerDto)).thenReturn(offer);
    when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
    when(tenderRepository.save(tender)).thenReturn(tender);
    tenderService.addOffer(1l, offerDto);

    assertTrue(tender.getOffers().contains(offer));
  }

  @Test
  public void addOfferOnlyOnce() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    OfferDto offerDto = new OfferDto(offer);

    Tender tender = new Tender(1, 2, "this is work description");

    when(offerService.create(offerDto)).thenReturn(offer);
    when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
    when(tenderRepository.save(tender)).thenReturn(tender);
    tenderService.addOffer(1l, offerDto);
    tenderService.addOffer(1l, offerDto);

    assertTrue(tender.getOffers().contains(offer));
    assertEquals(1, tender.getOffers().size());
  }

  @Test
  public void acceptedOffer() {
    Offer acceptedOffer = new Offer(1l, "this is description", new BigDecimal("1"), Currency.USD);
    acceptedOffer.setId(1);
    OfferDto acceptedOfferDto = new OfferDto(acceptedOffer);

    Offer offer1 = new Offer(1l, "this is description2", new BigDecimal("2"), Currency.USD);
    offer1.setId(2);
    OfferDto offer1Dto = new OfferDto(offer1);

    Offer offer2 = new Offer(1l, "this is description3", new BigDecimal("3"), Currency.USD);
    offer1.setId(3);
    OfferDto offer2Dto = new OfferDto(offer2);

    Tender tender = new Tender(1, 2, "this is work description");

    when(offerService.create(acceptedOfferDto)).thenReturn(acceptedOffer);
    doCallRealMethod().when(offerService).accept(acceptedOffer);
    doCallRealMethod().when(offerService).reject(offer1);
    doCallRealMethod().when(offerService).reject(offer2);
    when(offerService.create(offer1Dto)).thenReturn(offer1);
    when(offerService.create(offer2Dto)).thenReturn(offer2);
    when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
    when(tenderRepository.save(tender)).thenReturn(tender);

    tenderService.addOffer(1l, acceptedOfferDto);
    tenderService.addOffer(1l, offer1Dto);
    tenderService.addOffer(1l, offer2Dto);

    tenderService.acceptOffer(1l, 1l);

    assertEquals(acceptedOffer.getStatus(), OfferStatus.ACCEPTED);
    assertEquals(OfferStatus.REJECTED, offer1.getStatus());
    assertEquals(OfferStatus.REJECTED, offer2.getStatus());
  }

  @Test
  public void acceptNonExistingOfferException() {
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> {
          Offer acceptedOffer =
              new Offer(1l, "this is description", new BigDecimal("1"), Currency.USD);
          acceptedOffer.setId(1);
          OfferDto acceptedOfferDto = new OfferDto(acceptedOffer);

          Tender tender = new Tender(1, 2, "this is work description");

          when(offerService.create(acceptedOfferDto)).thenReturn(acceptedOffer);
          when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
          when(tenderRepository.save(tender)).thenReturn(tender);

          tenderService.addOffer(1l, acceptedOfferDto);

          tenderService.acceptOffer(1l, 2l);
        });
  }

  @Test
  public void getTenderByIssuerId() {
    Tender tender = new Tender(1l, 2l, "this is work description");
    TenderDto tenderDto = new TenderDto(tender);

    when(tenderRepository.findByIssuerId(2l)).thenReturn(Collections.singletonList(tender));

    Set<TenderDto> tenderDtoSet = tenderService.findByIssuerId(2l);
    assertEquals(1, tenderDtoSet.size());
    List<TenderDto> tenders = new ArrayList(tenderDtoSet);

    assertEquals(tenderDto.getIssuerId(), tenders.get(0).getIssuerId());
    assertEquals(tenderDto.getProjectId(), tenders.get(0).getProjectId());
    assertEquals(tenderDto.getWorkDescription(), tenders.get(0).getWorkDescription());
  }

  @Test
  public void getAllOffersForTender() {
    Offer offer1 = new Offer(1l, "this is description1", new BigDecimal("2"), Currency.USD);
    offer1.setId(1);
    OfferDto offer1Dto = new OfferDto(offer1);

    Offer offer2 = new Offer(1l, "this is description2", new BigDecimal("3"), Currency.USD);
    offer1.setId(2);
    OfferDto offer2Dto = new OfferDto(offer2);

    Tender tender = new Tender(1, 2, "this is work description");

    when(offerService.create(offer1Dto)).thenReturn(offer1);
    when(offerService.create(offer2Dto)).thenReturn(offer2);
    when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
    when(tenderRepository.save(tender)).thenReturn(tender);

    tenderService.addOffer(1l, offer1Dto);
    tenderService.addOffer(1l, offer2Dto);

    Set<OfferDto> allOffersForTender = tenderService.getAllOffersForTender(1l);

    assertEquals(2, allOffersForTender.size());
  }

  @Test
  public void getOffersForTenderAndBidder() {
    Offer offer1 = new Offer(1l, "this is description1", new BigDecimal("2"), Currency.USD);
    offer1.setId(1);
    OfferDto offer1Dto = new OfferDto(offer1);

    Offer offer2 = new Offer(2l, "this is description2", new BigDecimal("3"), Currency.USD);
    offer1.setId(2);
    OfferDto offer2Dto = new OfferDto(offer2);

    Tender tender = new Tender(1, 2, "this is work description");

    when(offerService.create(offer1Dto)).thenReturn(offer1);
    when(offerService.create(offer2Dto)).thenReturn(offer2);
    when(tenderRepository.findById(1l)).thenReturn(java.util.Optional.of(tender));
    when(tenderRepository.save(tender)).thenReturn(tender);

    tenderService.addOffer(1l, offer1Dto);
    tenderService.addOffer(1l, offer2Dto);

    Set<OfferDto> offers = tenderService.getOffersForTenderAndBidder(1l, 2l);

    assertEquals(1, offers.size());
  }
}
