package com.abhiram.stocktrader.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private Integer quantity;

    private Double averagePrice;

    /**
     * Owner of this holding.
     * Hidden from API responses.
     */
    @ManyToOne
    @JsonIgnore
    private User user;
}