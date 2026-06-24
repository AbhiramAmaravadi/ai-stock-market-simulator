package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.dto.AiAnalysisResponse;
import com.abhiram.stocktrader.dto.DashboardResponse;
import com.abhiram.stocktrader.service.AiAdvisorService;
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

    public AiController(
            PortfolioService portfolioService,
            AiAdvisorService aiAdvisorService) {

        this.portfolioService = portfolioService;
        this.aiAdvisorService = aiAdvisorService;
    }

    /**
     * Analyze user's portfolio.
     */
    @GetMapping("/analyze/{email}")
    public AiAnalysisResponse analyzePortfolio(
            @PathVariable String email) {

        DashboardResponse dashboard = portfolioService.getDashboard(email);

        String analysis = aiAdvisorService.analyzePortfolio(
                dashboard);

        return new AiAnalysisResponse(
                analysis);
    }
}