package com.abhiram.stocktrader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response returned by AI advisor.
 */
@Data
@AllArgsConstructor
public class AiAnalysisResponse {

    private String analysis;
}