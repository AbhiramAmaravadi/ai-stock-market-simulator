package com.abhiram.stocktrader.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioHoldingResponse {

    private Long id;
    private String symbol;
    private Integer quantity;
    private Double averagePrice;

    private Double currentPrice;
    private Double currentValue;
    private Double gainLoss;
    private Double gainPercent;
}