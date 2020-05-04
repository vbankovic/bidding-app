package com.homework.biddingapp.repository;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OfferRepositoryTest {

	@Autowired
	private OfferRepository offerRepository;

	private Offer firstOffer;
	private Offer secondOffer;

	@BeforeEach
	public void setUp() {
		firstOffer = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);
		secondOffer = new Offer(1l, "second offer", new BigDecimal("23"), Currency.EUR);

	}

	@Test
	public void saveOfferAndFindByBidderId() {
		firstOffer = offerRepository.save(firstOffer);
		secondOffer = offerRepository.save(secondOffer);
		assertThat(offerRepository.findByBidderId(firstOffer.getBidderId()).get().size()).isEqualTo(2);

	}

}