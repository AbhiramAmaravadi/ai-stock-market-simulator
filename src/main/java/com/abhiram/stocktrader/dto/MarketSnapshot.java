package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a live market snapshot
 * for a stock symbol.
 */
@Data
@AllArgsConstructor
public class MarketSnapshot {

    /**
     * Current market price.
     */
    private double currentPrice;

    /**
     * Absolute daily price change.
     */
    private double dailyChange;

    /**
     * Daily percentage change.
     */
    private double percentChange;

    /**
     * Opening price.
     */
    private double openPrice;

    /**
     * Highest price today.
     */
    private double highPrice;

    /**
     * Lowest price today.
     */
    private double lowPrice;

    /**
     * Previous market close.
     */
    private double previousClose;
}