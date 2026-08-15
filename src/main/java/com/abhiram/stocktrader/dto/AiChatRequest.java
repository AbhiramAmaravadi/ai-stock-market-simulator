package com.abhiram.stocktrader.dto;

import lombok.Data;

/**
 * Request sent to the AI investment assistant.
 */
@Data
public class AiChatRequest {

    /**
     * User email.
     */
    private String email;

    /**
     * User's investment question.
     */
    private String message;
}