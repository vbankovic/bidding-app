package com.homework.biddingapp.entity;

import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "bidder_id", nullable = false, updatable = false)
    private long bidderId;

    @Column(name = "description", nullable = true, length = 512)
    private String description;

    @Column(name = "price", nullable = false, updatable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency = Currency.CHF;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OfferStatus status = OfferStatus.PENDING;

    @ManyToOne
    private Tender tender;

    public Offer(long bidderId, String description, BigDecimal price, Currency currency) {
        this.bidderId = bidderId;
        this.description = description;
        this.price = price;
        this.currency = currency == null ? Currency.CHF : currency;
    }

}