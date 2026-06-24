package com.abhiram.stocktrader.dto;

import lombok.Data;

/**
 * Response received from Ollama.
 */
@Data
public class OllamaResponse {

    private String response;
}