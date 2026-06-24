package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.service.MarketDataService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/market")
public class MarketController {

    private final MarketDataService marketDataService;

    public MarketController(
            MarketDataService marketDataService) {

        this.marketDataService = marketDataService;
    }

    @GetMapping("/{symbol}")
    public Map<String, Object> getPrice(
            @PathVariable String symbol) {

        Double price = marketDataService
                .getCurrentPrice(symbol);

        return Map.of(
                "symbol", symbol,
                "price", price);
    }
}