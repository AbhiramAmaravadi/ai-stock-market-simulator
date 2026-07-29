package com.abhiram.stocktrader.dto;

import lombok.Data;

/**
 * Represents a single company returned
 * by Finnhub's Symbol Lookup API.
 */
@Data
public class SymbolResult {

    /**
     * Company name.
     */
    private String description;

    /**
     * Display ticker.
     */
    private String displaySymbol;

    /**
     * Official ticker symbol.
     */
    private String symbol;
}