package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.DashboardResponse;
import com.abhiram.stocktrader.dto.PortfolioAnalyticsResponse;
import com.abhiram.stocktrader.entity.PortfolioHolding;
import org.springframework.stereotype.Service;

/**
 * Calculates portfolio analytics that are used
 * by the AI advisor and dashboard.
 */
@Service
public class PortfolioAnalyticsService {
    private final MarketDataService marketDataService;

    public PortfolioAnalyticsService(
            MarketDataService marketDataService) {

        this.marketDataService = marketDataService;
    }

    /**
     * Calculates portfolio analytics.
     *
     * @param dashboard User dashboard data
     * @return Calculated portfolio analytics
     */
    public PortfolioAnalyticsResponse analyzePortfolio(
            DashboardResponse dashboard) {

        double diversificationScore = calculateDiversificationScore(
                dashboard.getHoldings().size());

        double concentrationScore = calculateConcentrationScore(dashboard);

        double liquidityScore = calculateLiquidityScore(dashboard);

        // Placeholder until we implement a real
        // volatility model.
        double volatilityScore = 50;

        double overallRiskScore = combineScores(
                diversificationScore,
                concentrationScore,
                liquidityScore,
                volatilityScore);
        // Calculate overall unrealized performance.
        double unrealizedGainLoss = calculateUnrealizedGainLoss(dashboard);

        double unrealizedReturnPercentage = calculateUnrealizedReturnPercentage(dashboard);

        String riskCategory = determineRiskCategory(overallRiskScore);

        return new PortfolioAnalyticsResponse(
                overallRiskScore,
                diversificationScore,
                concentrationScore,
                liquidityScore,
                volatilityScore,
                calculateLargestHoldingPercentage(dashboard),
                calculateCashAllocation(dashboard),
                dashboard.getHoldings().size(),
                unrealizedGainLoss,
                unrealizedReturnPercentage,
                riskCategory);
    }

    /**
     * Calculates diversification risk based on the
     * number of unique holdings in the portfolio.
     *
     * The returned value is normalized between
     * 0 and 100, where:
     *
     * 0 = Excellent diversification
     * 100 = Poor diversification
     */
    /**
     * Calculates diversification risk based on the
     * number of unique holdings.
     *
     * The diversification benefit follows a
     * diminishing returns curve.
     */
    private double calculateDiversificationScore(
            int holdings) {

        if (holdings <= 0) {
            return 100;
        }

        double score = 100 / Math.sqrt(holdings);

        return Math.min(score, 100);
    }

    /**
     * Calculates the percentage of the account
     * currently held as cash.
     */
    private double calculateCashAllocation(
            DashboardResponse dashboard) {

        if (dashboard.getTotalAccountValue() == 0) {
            return 100;
        }

        return (dashboard.getCashBalance()
                / dashboard.getTotalAccountValue()) * 100;
    }

    /**
     * Calculates what percentage of the invested
     * portfolio is represented by the largest holding.
     */
    private double calculateLargestHoldingPercentage(
            DashboardResponse dashboard) {

        double totalPortfolioValue = 0;
        double largestHoldingValue = 0;

        for (PortfolioHolding holding : dashboard.getHoldings()) {

            double currentPrice = marketDataService.getCurrentPrice(
                    holding.getSymbol());

            double positionValue = currentPrice * holding.getQuantity();

            totalPortfolioValue += positionValue;

            if (positionValue > largestHoldingValue) {
                largestHoldingValue = positionValue;
            }
        }

        if (totalPortfolioValue == 0) {
            return 0;
        }

        return (largestHoldingValue / totalPortfolioValue) * 100;
    }

    /**
     * Calculates concentration risk.
     *
     * Higher concentration means a single position
     * dominates the portfolio.
     */
    private double calculateConcentrationScore(
            DashboardResponse dashboard) {

        double largestHolding = calculateLargestHoldingPercentage(dashboard);

        return Math.min(largestHolding * 1.25, 100);
    }

    /**
     * Calculates liquidity risk.
     *
     * A portfolio holding approximately 15% cash is
     * considered well balanced. Risk increases as the
     * cash allocation moves further away from this
     * target in either direction.
     */
    private double calculateLiquidityScore(
            DashboardResponse dashboard) {

        double cash = calculateCashAllocation(dashboard);

        double deviation = Math.abs(cash - 15);

        return Math.min(deviation * 1.5, 100);
    }

    /**
     * Combines all individual analytics into
     * one overall portfolio risk score.
     */
    private double combineScores(
            double diversification,
            double concentration,
            double liquidity,
            double volatility) {

        double weightedScore = (diversification * 0.30)
                + (concentration * 0.30)
                + (liquidity * 0.20)
                + (volatility * 0.20);

        // Convert from 0-100 scale to 0-10 scale.
        return weightedScore / 10.0;
    }

    /**
     * Calculates the portfolio's total unrealized
     * gain or loss in dollars.
     */
    private double calculateUnrealizedGainLoss(
            DashboardResponse dashboard) {

        double gainLoss = 0;

        for (PortfolioHolding holding : dashboard.getHoldings()) {

            double currentPrice = marketDataService.getCurrentPrice(
                    holding.getSymbol());

            gainLoss += (currentPrice - holding.getAveragePrice())
                    * holding.getQuantity();
        }

        return gainLoss;
    }

    /**
     * Calculates the unrealized return percentage
     * of the invested portfolio.
     */
    private double calculateUnrealizedReturnPercentage(
            DashboardResponse dashboard) {

        double investedAmount = 0;

        for (PortfolioHolding holding : dashboard.getHoldings()) {

            investedAmount += holding.getAveragePrice()
                    * holding.getQuantity();
        }

        if (investedAmount == 0) {
            return 0;
        }

        return calculateUnrealizedGainLoss(dashboard)
                / investedAmount
                * 100;
    }

    /**
     * Converts the numerical risk score
     * into a user-friendly category.
     */
    private String determineRiskCategory(
            double score) {

        if (score < 3.5) {
            return "Low";
        }

        if (score < 6.5) {
            return "Moderate";
        }

        if (score < 8.5) {
            return "High";
        }

        return "Very High";
    }
}