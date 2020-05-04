package com.homework.biddingapp.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.homework.biddingapp.utils.TestUtils;
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
public class TenderEntityTest {

  @Autowired private TestEntityManager entityManager;

  private Tender tender;

  @BeforeEach
  public void setUp() {
    tender = new Tender(1l, 1l, "this work description");
  }

  @Test
  public void saveTender() {
    Tender savedTender = this.entityManager.persistAndFlush(tender);
    assertThat(savedTender.getProjectId()).isEqualTo(tender.getProjectId());
    assertThat(savedTender.getIssuerId()).isEqualTo(tender.getIssuerId());
    assertThat(savedTender.getWorkDescription()).isEqualTo(tender.getWorkDescription());
    assertThat(tender.getOffers()).isEmpty();
  }

  @Test
  public void createTenderWithNullWorkDescriptionException() {
    Assertions.assertThrows(
        javax.persistence.PersistenceException.class,
        () -> {
          Tender tender = new Tender(1l, 1l, null);
          this.entityManager.persistAndFlush(tender);
        });
  }

  @Test
  public void createTenderWithLongerDescriptionException() {
    Assertions.assertThrows(
        javax.persistence.PersistenceException.class,
        () -> {
          String description = TestUtils.getStringWithLength(513, '*');
          Tender tender = new Tender(1l, 1l, description);
          this.entityManager.persistAndFlush(tender);
        });
  }
}
