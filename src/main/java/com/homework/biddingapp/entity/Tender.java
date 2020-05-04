package com.homework.biddingapp.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
@Table(name = "tender")
public class Tender {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private long id;

  @Column(name = "project_id", nullable = false, updatable = false)
  private long projectId;

  @Column(name = "issuer_id", nullable = false, updatable = false)
  private long issuerId;

  @Column(name = "work_description", nullable = false, updatable = false, length = 512)
  private String workDescription;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "tender")
  private Set<Offer> offers = new HashSet<>();

  public Tender(long projectId, long issuerId, String workDescription) {
    this.projectId = projectId;
    this.issuerId = issuerId;
    this.workDescription = workDescription;
  }

}
