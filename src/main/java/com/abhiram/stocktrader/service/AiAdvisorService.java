package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.DashboardResponse;
import org.springframework.stereotype.Service;

/**
 * Uses Ollama to analyze portfolio.
 */
@Service
public class AiAdvisorService {

    private final OllamaService ollamaService;

    public AiAdvisorService(
            OllamaService ollamaService) {

        this.ollamaService = ollamaService;
    }

    /**
     * Generates AI investment advice.
     */
    public String analyzePortfolio(
            DashboardResponse dashboard) {

        String prompt = """
                You are a professional investment advisor.

                Analyze the following portfolio.

                Cash Balance:
                %s

                Portfolio Value:
                %s

                Total Account Value:
                %s

                Holdings:
                %s

                Provide:

                1. Risk Level
                2. Diversification Analysis
                3. Strengths
                4. Weaknesses
                5. Recommendations
                """
                .formatted(
                        dashboard.getCashBalance(),
                        dashboard.getPortfolioValue(),
                        dashboard.getTotalAccountValue(),
                        dashboard.getHoldings());

        return ollamaService.generate(prompt);
    }
}