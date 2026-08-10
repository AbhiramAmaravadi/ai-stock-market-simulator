package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO sent to the frontend for each stock in the user's portfolio.
 *
 * Unlike the PortfolioHolding entity, this class contains both:
 * 1. Persistent data stored in the database (id, symbol, quantity,
 * averagePrice)
 * 2. Computed values generated at request time (currentPrice, gain/loss, etc.)
 *
 * This keeps the database normalized while still providing the frontend with
 * all the information it needs in a single API response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioHoldingResponse {

    // Database ID of the holding
    private Long id;

    // Stock ticker symbol (e.g. AAPL, TSLA)
    private String symbol;

    // Number of shares currently owned
    private Integer quantity;

    // Average purchase price per share (cost basis)
    private Double averagePrice;

    // Current live market price per share
    private Double currentPrice;

    // Current market value of this holding
    // Formula: currentPrice × quantity
    private Double currentValue;

    // Total unrealized profit/loss in dollars
    // Formula: (currentPrice - averagePrice) × quantity
    private Double gainLoss;

    // Unrealized profit/loss as a percentage
    // Formula: ((currentPrice - averagePrice) / averagePrice) × 100
    private Double gainPercent;
}