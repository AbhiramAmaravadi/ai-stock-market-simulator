package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.DashboardResponse;
import com.abhiram.stocktrader.dto.RiskScoreResponse;
import com.abhiram.stocktrader.entity.PortfolioHolding;
import org.springframework.stereotype.Service;

/**
 * Calculates objective portfolio risk metrics.
 *
 * Unlike the AI model, this service performs
 * deterministic financial calculations.
 */
@Service
public class PortfolioRiskService {

    private final MarketDataService marketDataService;

    public PortfolioRiskService(
            MarketDataService marketDataService) {

        this.marketDataService = marketDataService;
    }

    /**
     * Calculates portfolio risk statistics.
     *
     * @param dashboard User portfolio data
     * @return Calculated risk metrics
     */
    public RiskScoreResponse calculateRisk(
            DashboardResponse dashboard) {

        double totalPortfolioValue = 0;
        double largestHoldingValue = 0;

        // Calculate current value of every holding.
        for (PortfolioHolding holding : dashboard.getHoldings()) {

            double currentPrice = marketDataService.getCurrentPrice(
                    holding.getSymbol());

            double positionValue = currentPrice * holding.getQuantity();

            totalPortfolioValue += positionValue;

            if (positionValue > largestHoldingValue) {
                largestHoldingValue = positionValue;
            }
        }

        // Calculate cash allocation.
        double cashAllocation = 0;

        if (dashboard.getTotalAccountValue() > 0) {

            cashAllocation = (dashboard.getCashBalance()
                    / dashboard.getTotalAccountValue()) * 100;
        }

        // Calculate largest holding percentage.
        double largestHoldingPercentage = 0;

        if (totalPortfolioValue > 0) {

            largestHoldingPercentage = (largestHoldingValue
                    / totalPortfolioValue) * 100;
        }

        int numberOfHoldings = dashboard.getHoldings().size();

        /*
         * Simple scoring model.
         *
         * This can later be replaced with a more
         * sophisticated financial risk algorithm.
         */
        double riskScore = 0;

        // Very little cash increases risk.
        if (cashAllocation < 10)
            riskScore += 2;

        // Highly concentrated portfolio.
        if (largestHoldingPercentage > 50)
            riskScore += 4;

        // Few holdings means less diversification.
        if (numberOfHoldings < 5)
            riskScore += 2;

        // Single-stock portfolio.
        if (numberOfHoldings == 1)
            riskScore += 2;

        // Keep score within 0-10.
        riskScore = Math.min(riskScore, 10);

        return new RiskScoreResponse(
                riskScore,
                cashAllocation,
                largestHoldingPercentage,
                numberOfHoldings);
    }
}