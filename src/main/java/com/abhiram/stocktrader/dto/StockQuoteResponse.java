package com.abhiram.stocktrader.dto;

import lombok.Data;

@Data
public class StockQuoteResponse {

    private Double c; // current price
    private Double h; // high
    private Double l; // low
    private Double o; // open
    private Double pc; // previous close
    private Double d; // change
    private Double dp; // change percent
}