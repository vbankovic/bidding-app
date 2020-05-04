package com.homework.biddingapp.web;

import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.model.TenderDto;
import com.homework.biddingapp.service.TenderService;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tenders")
public class TenderController {

  @Autowired private TenderService service;

  @PostMapping
  public ResponseEntity<TenderDto> create(@RequestBody @Valid TenderDto tenderDto) {
    return ResponseEntity.ok(service.create(tenderDto));
  }

  @PostMapping("/{tenderId}/offers")
  public ResponseEntity<OfferDto> addOffer(
      @PathVariable long tenderId, @RequestBody @Valid OfferDto createDto) {
    return ResponseEntity.ok(service.addOffer(tenderId, createDto));
  }

  @PutMapping("/{tenderId}/offers/{offerId}/accept")
  public ResponseEntity<Void> acceptOffer(@PathVariable long tenderId, @PathVariable long offerId) {
    service.acceptOffer(tenderId, offerId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<Set<TenderDto>> findAllByIssuer(
      @RequestParam(value = "issuer_id", required = false) Long issuerId) {
    return ResponseEntity.ok(service.findByIssuerId(issuerId));
  }

  @GetMapping("/{id}/offers")
  public ResponseEntity<Set<OfferDto>> getAllOffers(
      @PathVariable("id") long tenderId,
      @RequestParam(value = "bidder_id", required = false) Long bidderId) {

    if (bidderId != null) {
      return ResponseEntity.ok(service.getOffersForTenderAndBidder(tenderId, bidderId));
    } else {
      return ResponseEntity.ok(service.getAllOffersForTender(tenderId));
    }
  }
}
