package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AI investment assistant response.
 */
@Data
@AllArgsConstructor
public class AiChatResponse {

    private String response;
}