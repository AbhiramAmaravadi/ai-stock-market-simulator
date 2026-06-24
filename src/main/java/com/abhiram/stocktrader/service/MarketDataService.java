package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.StockQuoteResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataService {

    @Value("${finnhub.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Double getCurrentPrice(String symbol) {

        String url = "https://finnhub.io/api/v1/quote?symbol="
                + symbol
                + "&token="
                + apiKey;

        StockQuoteResponse response = restTemplate.getForObject(
                url,
                StockQuoteResponse.class);

        return response.getC();
    }
}