package com.homework.biddingapp.web;

import java.util.Set;

import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/offers")
public class OfferController {

  @Autowired
  private OfferService service;

  @GetMapping(path = "/{bidderId}")
  public ResponseEntity<Set<OfferDto>> getOffersByBidderId(@PathVariable long bidderId) {
    return ResponseEntity.ok(service.getOffersByBidderId(bidderId));
  }
}
