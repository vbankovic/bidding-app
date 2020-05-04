package com.homework.biddingapp.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.biddingapp.entity.Currency;
import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.Tender;
import com.homework.biddingapp.repository.OfferRepository;
import com.homework.biddingapp.repository.TenderRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase
@Transactional
public class TenderControllerTest {

  @Autowired private TenderRepository tenderRepository;

  @Autowired private OfferRepository offerRepository;

  @Autowired private MockMvc mockMvc;

  private Tender tender;
  private Offer offer;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setUp() {
    offer = new Offer(1l, "first offer", new BigDecimal("1"), Currency.USD);

    tender = new Tender(1l, 1l, "this work description");
    tender.getOffers().add(offer);
    tender = tenderRepository.save(tender);
  }

  @BeforeEach
  public void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext) //
            .apply(documentationConfiguration(restDocumentation))
            .build(); //
  }

  @AfterEach
  public void tearDown() {
    offerRepository.deleteAll();
    tenderRepository.deleteAll();
  }

  @Test
  public void createTender() throws Exception {
    String jsonRequest =
        "{"
            + "  \"project_id\": 1,"
            + "  \"issuer_id\": 2,"
            + "  \"work_description\": \"this is work description\"\n"
            + "}";

    JsonNode jsonRequestObject = objectMapper.readTree(jsonRequest);
    int expectedProjectId = jsonRequestObject.get("project_id").asInt();
    int expectedIssuerId = jsonRequestObject.get("issuer_id").asInt();
    String expectedWorkDescription = jsonRequestObject.get("work_description").asText();

    mockMvc
        .perform(post("/v1/tenders").contentType(APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("project_id", is(expectedProjectId)))
        .andExpect(jsonPath("issuer_id", is(expectedIssuerId)))
        .andExpect(jsonPath("work_description", is(expectedWorkDescription)))
        .andDo(document("create_tender"));
  }

  @Test
  public void addOffer() throws Exception {
    String jsonRequest =
        "{"
            + " \"bidder_id\": 1,"
            + "  \"price\": \"10000\","
            + "  \"currency\": \"USD\","
            + "  \"description\": 10000"
            + "}";

    mockMvc
        .perform(
            post("/v1/tenders/{id}/offers", tender.getId())
                .contentType(APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(status().isOk())
        .andDo(document("addOffer"));
  }

  @Test
  public void acceptOffer() throws Exception {
    mockMvc
        .perform(
            put("/v1/tenders/{tenderId}/offers/{offerId}/accept", tender.getId(), offer.getId()))
        .andExpect(status().isOk())
        .andDo(document("acceptOffer"));
  }

  @Test
  public void findAllByIssuer() throws Exception {
    mockMvc
        .perform(get("/v1/tenders").param("issuer_id", String.valueOf(tender.getIssuerId())))
        .andExpect(status().isOk())
        .andDo(document("findAllByIssuer"));
  }

  @Test
  public void getAllOffers() throws Exception {
    mockMvc
        .perform(
            get("/v1/tenders/{id}/offers", tender.getId())
                .param("bidder_id", String.valueOf(offer.getBidderId())))
        .andExpect(status().isOk())
        .andDo(document("getAllOffers"));
  }

  @Test
  public void createEmptyBody() throws Exception {
    mockMvc
        .perform(post("/v1/tenders").contentType(APPLICATION_JSON).content(""))
        .andExpect(status().isBadRequest());
  }
}
