package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.OllamaRequest;
import com.abhiram.stocktrader.dto.OllamaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for communicating with Ollama.
 */
@Service
public class OllamaService {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    /**
     * Sends prompt to Llama model.
     */
    public String generate(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        OllamaRequest request = new OllamaRequest(
                "llama3.2",
                prompt,
                false);

        OllamaResponse response = restTemplate.postForObject(
                OLLAMA_URL,
                request,
                OllamaResponse.class);

        return response.getResponse();
    }
}