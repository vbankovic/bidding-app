package com.homework.biddingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.OfferStatus;
import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.repository.OfferRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

  @InjectMocks private OfferService offerService;

  @Mock private OfferRepository offerRepository;

  @Mock private Offer firstOffer;

  @Mock private Offer secondOffer;

  @Test
  public void createOffer() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    OfferDto offerDto = mock(OfferDto.class);
    when(offerDto.getBidderId()).thenReturn(1L);
    when(offerDto.getPrice()).thenReturn("2");
    when(offerDto.getCurrency()).thenReturn("USD");
    when(offerDto.getDescription()).thenReturn("this is description");
    when(offerRepository.save(any())).thenReturn(offer);
    offer = offerService.create(offerDto);

    assertEquals(offerDto.getBidderId(), offer.getBidderId());
    assertEquals(offerDto.getPrice(), offer.getPrice().toString());
    assertEquals(offerDto.getCurrency(), offer.getCurrency().name());
    assertEquals(offerDto.getDescription(), offer.getDescription());
  }

  @Test
  public void pendingOffer() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    assertEquals(offer.getStatus(), OfferStatus.PENDING);
  }

  @Test
  public void acceptOffer() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    offerService.accept(offer);
    assertTrue(offerService.isAccepted(offer));
  }

  @Test
  public void rejectOffer() {
    Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
    offerService.reject(offer);
    assertEquals(offer.getStatus(), OfferStatus.REJECTED);
  }

  @Test
  public void rejectAcceptedException() {
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          Offer offer = new Offer(1l, "this is description", new BigDecimal("2"), Currency.USD);
          offerService.accept(offer);
          offerService.reject(offer);
        });
  }

  @Test
  public void getByBidderId() {
    long bidderId = 1;
    Optional<List<Offer>> optionalOffers =
        Optional.of(Arrays.asList(new Offer[] {firstOffer, secondOffer}));
    when(offerRepository.findByBidderId(bidderId)).thenReturn(optionalOffers);

    Set<Offer> offers = offerService.getByBidderId(bidderId);

    assertEquals(2, offers.size());
    assertTrue(offers.contains(firstOffer));
    assertTrue(offers.contains(secondOffer));
  }
}
