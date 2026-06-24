package com.abhiram.stocktrader.controller;

import com.abhiram.stocktrader.dto.BuyStockRequest;
import com.abhiram.stocktrader.dto.PortfolioValueResponse;
import com.abhiram.stocktrader.dto.SellStockRequest;
import com.abhiram.stocktrader.entity.PortfolioHolding;
import com.abhiram.stocktrader.entity.Transaction;
import com.abhiram.stocktrader.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.abhiram.stocktrader.dto.DashboardResponse;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(
            PortfolioService portfolioService) {

        this.portfolioService = portfolioService;
    }

    /**
     * Buy stock using live market price.
     */
    @PostMapping("/buy")
    public String buyStock(
            @RequestBody BuyStockRequest request) {

        portfolioService.buyStock(request);

        return "Stock purchased successfully";
    }

    /**
     * View current portfolio holdings.
     */
    @GetMapping("/{email}")
    public List<PortfolioHolding> getPortfolio(
            @PathVariable String email) {

        return portfolioService.getPortfolio(email);
    }

    /**
     * View transaction history.
     */
    @GetMapping("/transactions/{email}")
    public List<Transaction> getTransactions(
            @PathVariable String email) {

        return portfolioService.getTransactions(email);
    }

    /**
     * Sell stock.
     */
    @PostMapping("/sell")
    public String sellStock(
            @RequestBody SellStockRequest request) {

        portfolioService.sellStock(request);

        return "Stock sold successfully";
    }

    /**
     * Calculate total account value.
     */
    @GetMapping("/value/{email}")
    public PortfolioValueResponse getPortfolioValue(
            @PathVariable String email) {

        return portfolioService.getPortfolioValue(email);
    }

    /**
     * Returns dashboard information.
     */
    @GetMapping("/dashboard/{email}")
    public DashboardResponse getDashboard(
            @PathVariable String email) {

        return portfolioService.getDashboard(email);
    }
}