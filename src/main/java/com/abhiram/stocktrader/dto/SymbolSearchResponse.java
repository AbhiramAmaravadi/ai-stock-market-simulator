package com.abhiram.stocktrader.dto;

import lombok.Data;

import java.util.List;

/**
 * Response returned by Finnhub's
 * Symbol Lookup API.
 */
@Data
public class SymbolSearchResponse {

    /**
     * Number of matching companies.
     */
    private int count;

    /**
     * Matching companies.
     */
    private List<SymbolResult> result;
}