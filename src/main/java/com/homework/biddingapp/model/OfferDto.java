package com.homework.biddingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homework.biddingapp.entity.Offer;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfferDto {

  @JsonProperty("id")
  private long id;

  @NotNull(message = "bidder_id is mandatory")
  @JsonProperty("bidder_id")
  private long bidderId;

  @JsonProperty("description")
  @Size(max = 512, message = "description cannot be larger than 512 characters")
  private String description;

  @NotBlank(message = "price is mandatory")
  @JsonProperty("price")
  private String price;

  @NotBlank(message = "currency is mandatory")
  @JsonProperty("currency")
  private String currency;

  @JsonProperty("status")
  private String status;

  public OfferDto(Offer offer) {
    id = offer.getId();
    bidderId = offer.getBidderId();
    description = offer.getDescription();
    price = String.valueOf(offer.getPrice());
    currency = String.valueOf(offer.getCurrency());
    status = String.valueOf(offer.getStatus());
  }
}
