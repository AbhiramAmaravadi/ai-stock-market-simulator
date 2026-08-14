package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.dto.StockQuoteResponse;
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

        StockQuoteResponse quote = marketDataService.getQuote(symbol);

        return Map.of(
                "symbol", symbol,
                "price", quote.getC(),
                "high", quote.getH(),
                "low", quote.getL(),
                "open", quote.getO(),
                "previousClose", quote.getPc(),
                "change", quote.getD(),
                "changePercent", quote.getDp());
    }
}