package com.homework.biddingapp.web;

import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.Tender;
import com.homework.biddingapp.repository.OfferRepository;
import com.homework.biddingapp.repository.TenderRepository;
import com.homework.biddingapp.service.OfferService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OfferControllerTest {

  @Autowired
  OfferService service;

  @Autowired
  private TenderRepository tenderRepository;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private MockMvc mockMvc;

  private long bidderId;

  @BeforeAll
  @Transactional
  public void setUp() {
    Offer offer1 = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);
    Offer offer2 = new Offer(1l, "second offer", new BigDecimal("2"), Currency.USD);

    bidderId = offer1.getBidderId();

    Tender tender = new Tender(1l, 1l, "this work description");
    tender.getOffers().add(offer1);
    tender.getOffers().add(offer2);
    tenderRepository.save(tender);
  }

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext) //
                                  .apply(documentationConfiguration(restDocumentation)).build(); //
  }

  @AfterAll
  public void tearDown() {
    offerRepository.deleteAll();
    tenderRepository.deleteAll();
  }

  @Test
  public void getOffersByBidderId() throws Exception {
    mockMvc.perform(get("/v1/offers/{bidder_id}", bidderId))
        .andExpect(status().isOk())
        .andDo(document("getOffersByBidderId"));
  }
}