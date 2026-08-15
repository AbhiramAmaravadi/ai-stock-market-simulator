package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.dto.AiAnalysisResponse;
import com.abhiram.stocktrader.dto.AiChatRequest;
import com.abhiram.stocktrader.dto.AiChatResponse;
import com.abhiram.stocktrader.dto.DashboardResponse;
import com.abhiram.stocktrader.service.AiAdvisorService;
import com.abhiram.stocktrader.service.OllamaService;
import com.abhiram.stocktrader.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

/**
 * AI investment advisor endpoints.
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private final PortfolioService portfolioService;
    private final AiAdvisorService aiAdvisorService;
    private final OllamaService ollamaService;

    public AiController(
            PortfolioService portfolioService,
            AiAdvisorService aiAdvisorService,
            OllamaService ollamaService) {

        this.portfolioService = portfolioService;
        this.aiAdvisorService = aiAdvisorService;
        this.ollamaService = ollamaService;
    }

    /**
     * Analyze user's portfolio.
     */
    @GetMapping("/analyze/{email}")
    public AiAnalysisResponse analyzePortfolio(
            @PathVariable String email) {

        DashboardResponse dashboard = portfolioService.getDashboard(email);

        String analysis = aiAdvisorService.analyzePortfolio(dashboard);

        return new AiAnalysisResponse(analysis);
    }

    /**
     * Answer an investment question using Llama.
     */
    @PostMapping("/chat")
    public AiChatResponse chat(
            @RequestBody AiChatRequest request) {

        String prompt = """
                You are an AI investment assistant for a stock market simulator.

                User email:
                %s

                User's investment question:
                %s

                Provide a clear, helpful answer.
                Explain your reasoning briefly.
                Do not claim to know the user's personal financial
                circumstances beyond the information provided.
                This is educational information, not personalized
                financial advice.
                """
                .formatted(
                        request.getEmail(),
                        request.getMessage());

        String response = ollamaService.generate(prompt);

        return new AiChatResponse(response);
    }
}