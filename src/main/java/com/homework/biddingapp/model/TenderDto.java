package com.homework.biddingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.stream.Collectors;

import com.homework.biddingapp.entity.Tender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TenderDto {

  @JsonProperty("id")
  private long id;

  @NotNull(message = "project_id is mandatory")
  @JsonProperty("project_id")
  private long projectId;

  @NotNull(message = "issuer_id is mandatory")
  @JsonProperty("issuer_id")
  private long issuerId;

  @NotBlank(message = "work_description is mandatory")
  @Size(max = 512, message = "work_description cannot be larger than 512 characters")
  @JsonProperty("work_description")
  private String workDescription;

  @JsonProperty private Set<OfferDto> offers;

  public TenderDto(Tender tender) {
    id = tender.getId();
    projectId = tender.getProjectId();
    issuerId = tender.getIssuerId();
    workDescription = tender.getWorkDescription();
    offers = tender.getOffers().stream().map(OfferDto::new).collect(Collectors.toSet());
  }
}
