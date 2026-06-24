package com.abhiram.stocktrader.dto;

import lombok.Data;

@Data
public class SellStockRequest {

    private String email;
    private String symbol;
    private Integer quantity;
}