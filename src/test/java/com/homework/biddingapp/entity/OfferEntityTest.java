package com.homework.biddingapp.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.homework.biddingapp.utils.TestUtils;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OfferEntityTest {

  @Autowired private TestEntityManager entityManager;

  private Offer firstOffer;

  @BeforeEach
  public void setUp() {
    firstOffer = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);
  }

  @Test
  public void saveOffer() {
    Offer savedOffer = this.entityManager.persistAndFlush(firstOffer);
    assertThat(savedOffer.getBidderId()).isEqualTo(firstOffer.getBidderId());
    assertThat(savedOffer.getDescription()).isEqualTo(firstOffer.getDescription());
    assertThat(savedOffer.getPrice()).isEqualTo(firstOffer.getPrice());
    assertThat(savedOffer.getCurrency()).isEqualTo(firstOffer.getCurrency());
    assertThat(savedOffer.getStatus()).isEqualTo(OfferStatus.PENDING);
    assertThat(savedOffer.getId()).isNotNull();
  }

  @Test
  public void createOfferWithNullPriceException() {
    Assertions.assertThrows(
        javax.persistence.PersistenceException.class,
        () -> {
          Offer offer = new Offer(1l, "second offer", null, Currency.CHF);
          this.entityManager.persistAndFlush(offer);
        });
  }

  @Test
  public void createOfferWithNullCurrency() {
    Offer offer = new Offer(1l, "second offer", new BigDecimal("1"), null);
    Offer savedOffer = this.entityManager.persistAndFlush(offer);
    assertThat(savedOffer.getCurrency()).isEqualTo(Currency.CHF);
  }

  @Test
  public void createOfferWithLongerDescriptionException() {
    Assertions.assertThrows(
        javax.persistence.PersistenceException.class,
        () -> {
          String description = TestUtils.getStringWithLength(513, '*');
          Offer offer = new Offer(1l, description, new BigDecimal("23"), Currency.CHF);
          this.entityManager.persistAndFlush(offer);
        });
  }
}
