package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request sent to Ollama.
 */
@Data
@AllArgsConstructor
public class OllamaRequest {

    private String model;

    private String prompt;

    private boolean stream;
}