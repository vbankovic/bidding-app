package com.homework.biddingapp.repository;

import com.homework.biddingapp.entity.Tender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TenderRepositoryTest {

	@Autowired
	private TenderRepository tenderRepository;

	private Tender firstTender;
	private Tender secondTender;

	@BeforeEach
	public void setUp() {
		firstTender = new Tender(1l, 1l, "this is work description");
		secondTender = new Tender(1l, 1l, "this is second work description");
	}

	@Test
	public void saveTenderAndFindByIssuerId() {
		firstTender = tenderRepository.save(firstTender);
		secondTender = tenderRepository.save(secondTender);
		assertThat(tenderRepository.findByIssuerId(firstTender.getIssuerId()).size()).isEqualTo(2);
	}

}