package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PortfolioValueResponse {

    private Double cashBalance;
    private Double portfolioValue;
    private Double totalAccountValue;
}