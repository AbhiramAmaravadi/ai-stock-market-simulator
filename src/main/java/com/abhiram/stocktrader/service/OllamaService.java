package com.abhiram.stocktrader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Service for communicating with Llama through Groq.
 */
@Service
public class OllamaService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL = "llama-3.1-8b-instant";

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Sends prompt to Llama through Groq.
     */
    public String generate(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt);

        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", List.of(message),
                "temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                GROQ_URL,
                HttpMethod.POST,
                request,
                Map.class);

        Map body = response.getBody();

        if (body == null || body.get("choices") == null) {
            throw new RuntimeException("Invalid response from Groq");
        }

        List choices = (List) body.get("choices");

        if (choices.isEmpty()) {
            throw new RuntimeException("Groq returned no choices");
        }

        Map choice = (Map) choices.get(0);
        Map messageResponse = (Map) choice.get("message");

        return (String) messageResponse.get("content");
    }
}