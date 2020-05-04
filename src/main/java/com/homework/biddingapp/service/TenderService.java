package com.homework.biddingapp.service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.homework.biddingapp.entity.Offer;
import com.homework.biddingapp.entity.Tender;
import com.homework.biddingapp.model.OfferDto;
import com.homework.biddingapp.model.TenderDto;
import com.homework.biddingapp.repository.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenderService {

  @Autowired
  private TenderRepository tenderRepository;

  @Autowired
  private OfferService offerService;

  @Transactional
  public TenderDto create(TenderDto tenderDto) {
    Tender tender = tenderRepository.save(createTenderFromDTO(tenderDto));
    return new TenderDto(tender);
  }

  @Transactional
  public OfferDto addOffer(long tenderId, OfferDto offerDto) {
    Tender tender = getTender(tenderId);
    Offer offer = offerService.create(offerDto);
    tender.getOffers().add(offer);
    tenderRepository.save(tender);
    return new OfferDto(offer);
  }

  @Transactional
  public void acceptOffer(long tenderId, long offerId) {
    Tender tender = getTender(tenderId);
    acceptOffer(tender, offerId);
    tenderRepository.save(tender);
  }

  public Set<TenderDto> findByIssuerId(long issuerId) {
    HashSet<Tender> tenders = new HashSet<>(tenderRepository.findByIssuerId(issuerId));
    return tenders.stream() //
                  .map(TenderDto::new) //
                  .collect(Collectors.toSet()); //
  }

  public Set<OfferDto> getAllOffersForTender(long tenderId) {
    Tender tender = getTender(tenderId);
    return tender.getOffers().stream() //
                             .map(OfferDto::new) //
                             .collect(Collectors.toSet()); //
  }

  public Set<OfferDto> getOffersForTenderAndBidder(long tenderId, long bidderId) {
    return getAllOffersForTender(tenderId).stream() //
                                          .filter(offerDto -> offerDto.getBidderId() == bidderId) //
                                          .collect(Collectors.toSet()); //
  }

  private Tender getTender(long tenderId) {
    return tenderRepository.findById(tenderId) //
        .orElseThrow( //
            () -> new NoSuchElementException(String.format("Tender with id: %d do not exist", tenderId)));
  }

  private void acceptOffer(Tender tender, long offerId) {
    acceptIfPresent(tender, offerId);
    rejectOthers(tender, offerId);
  }

  private void acceptIfPresent(Tender tender, long offerId) {
    tender.getOffers().stream()
            .filter(offer -> offer.getId() == offerId)
            .findFirst()
            .ifPresentOrElse(
                    (offer) -> offerService.accept(offer),
                    () -> {
                      throw new NoSuchElementException(
                              String.format("Offer with id: %d is not added to tender", offerId));
                    });
  }

  private void rejectOthers(Tender tender, long offerId) {
    tender.getOffers().stream()
            .filter(offer -> offer.getId() != offerId)
            .forEach((offer) -> offerService.reject(offer));
  }

  private Tender createTenderFromDTO(TenderDto dto) {
    return new Tender(dto.getProjectId(), dto.getIssuerId(), dto.getWorkDescription());
  }
}
