package com.abhiram.stocktrader.dto;

import com.abhiram.stocktrader.entity.PortfolioHolding;
import com.abhiram.stocktrader.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Dashboard summary returned to frontend.
 */
@Data
@AllArgsConstructor
public class DashboardResponse {

    private Double cashBalance;

    private Double portfolioValue;

    private Double totalAccountValue;

    private List<PortfolioHolding> holdings;

    private List<Transaction> transactions;
}