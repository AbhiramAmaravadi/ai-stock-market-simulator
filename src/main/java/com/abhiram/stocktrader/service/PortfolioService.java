package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.BuyStockRequest;
import com.abhiram.stocktrader.dto.DashboardResponse;
import com.abhiram.stocktrader.dto.PortfolioHoldingResponse;
import com.abhiram.stocktrader.dto.SellStockRequest;
import com.abhiram.stocktrader.entity.PortfolioHolding;
import com.abhiram.stocktrader.entity.Transaction;
import com.abhiram.stocktrader.entity.User;
import com.abhiram.stocktrader.repository.PortfolioHoldingRepository;
import com.abhiram.stocktrader.repository.TransactionRepository;
import com.abhiram.stocktrader.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.abhiram.stocktrader.dto.PortfolioValueResponse;
import org.springframework.transaction.annotation.Transactional;

//import com.abhiram.stocktrader.dto.DashboardResponse;
@Service
public class PortfolioService {

        private final UserRepository userRepository;
        private final PortfolioHoldingRepository holdingRepository;
        private final TransactionRepository transactionRepository;
        private final MarketDataService marketDataService;

        public PortfolioService(
                        UserRepository userRepository,
                        PortfolioHoldingRepository holdingRepository,
                        TransactionRepository transactionRepository,
                        MarketDataService marketDataService) {

                this.userRepository = userRepository;
                this.holdingRepository = holdingRepository;
                this.transactionRepository = transactionRepository;
                this.marketDataService = marketDataService;
        }

        public void buyStock(BuyStockRequest request) {

                User user = userRepository.findByEmail(
                                request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Double currentPrice = marketDataService.getCurrentPrice(
                                request.getSymbol());

                Double totalCost = currentPrice * request.getQuantity();

                if (user.getCashBalance() < totalCost) {
                        throw new RuntimeException(
                                        "Insufficient funds");
                }

                user.setCashBalance(
                                user.getCashBalance() - totalCost);

                userRepository.save(user);

                PortfolioHolding holding = holdingRepository.findByUserAndSymbol(
                                user,
                                request.getSymbol())
                                .orElse(
                                                PortfolioHolding.builder()
                                                                .user(user)
                                                                .symbol(request.getSymbol())
                                                                .quantity(0)
                                                                .averagePrice(0.0)
                                                                .build());

                int oldQuantity = holding.getQuantity();
                double oldAveragePrice = holding.getAveragePrice();

                int newQuantity = request.getQuantity();
                double newPurchasePrice = currentPrice;

                int totalQuantity = oldQuantity + newQuantity;

                double newAveragePrice;

                if (oldQuantity == 0) {
                        newAveragePrice = newPurchasePrice;
                } else {
                        double combinedCost = (oldQuantity * oldAveragePrice)
                                        + (newQuantity * newPurchasePrice);

                        newAveragePrice = combinedCost / totalQuantity;
                }

                holding.setQuantity(totalQuantity);
                holding.setAveragePrice(newAveragePrice);

                holdingRepository.save(holding);
                Transaction transaction = Transaction.builder()
                                .user(user)
                                .symbol(request.getSymbol())
                                .quantity(request.getQuantity())
                                .price(currentPrice)
                                .type("BUY")
                                .timestamp(LocalDateTime.now())
                                .build();

                transactionRepository.save(transaction);
        }

        public List<PortfolioHoldingResponse> getPortfolio(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<PortfolioHolding> holdings = holdingRepository.findByUser(user);

                return holdings.stream().map(holding -> {

                        Double currentPrice = marketDataService.getCurrentPrice(
                                        holding.getSymbol());

                        Double currentValue = currentPrice * holding.getQuantity();

                        Double gainLoss = (currentPrice - holding.getAveragePrice())
                                        * holding.getQuantity();

                        Double gainPercent = holding.getAveragePrice() == 0
                                        ? 0.0
                                        : ((currentPrice - holding.getAveragePrice())
                                                        / holding.getAveragePrice()) * 100;

                        return PortfolioHoldingResponse.builder()
                                        .id(holding.getId())
                                        .symbol(holding.getSymbol())
                                        .quantity(holding.getQuantity())
                                        .averagePrice(holding.getAveragePrice())
                                        .currentPrice(currentPrice)
                                        .currentValue(currentValue)
                                        .gainLoss(gainLoss)
                                        .gainPercent(gainPercent)
                                        .build();

                }).toList();
        }

        public List<Transaction> getTransactions(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return transactionRepository.findByUser(user);
        }

        @Transactional
        public void sellStock(SellStockRequest request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                PortfolioHolding holding = holdingRepository.findByUserAndSymbol(
                                user,
                                request.getSymbol())
                                .orElseThrow(() -> new RuntimeException("Stock not owned"));

                if (holding.getQuantity() < request.getQuantity()) {
                        throw new RuntimeException("Not enough shares");
                }

                Double currentPrice = marketDataService.getCurrentPrice(request.getSymbol());

                Double saleValue = currentPrice * request.getQuantity();

                // Credit user's cash balance
                user.setCashBalance(user.getCashBalance() + saleValue);
                userRepository.save(user);

                // Reduce the holding quantity
                int remainingQuantity = holding.getQuantity() - request.getQuantity();

                if (remainingQuantity == 0) {
                        holdingRepository.delete(holding);
                } else {
                        holding.setQuantity(remainingQuantity);
                        // Do NOT update averagePrice when selling
                        holdingRepository.save(holding);
                }

                // Record the transaction
                Transaction transaction = Transaction.builder()
                                .user(user)
                                .symbol(request.getSymbol())
                                .quantity(request.getQuantity())
                                .price(currentPrice)
                                .type("SELL")
                                .timestamp(java.time.LocalDateTime.now())
                                .build();

                transactionRepository.save(transaction);
        }

        public PortfolioValueResponse getPortfolioValue(
                        String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<PortfolioHolding> holdings = holdingRepository.findByUser(user);

                double portfolioValue = 0.0;

                for (PortfolioHolding holding : holdings) {

                        double currentPrice = marketDataService.getCurrentPrice(
                                        holding.getSymbol());

                        portfolioValue += currentPrice * holding.getQuantity();
                }

                double totalAccountValue = user.getCashBalance() + portfolioValue;

                return new PortfolioValueResponse(
                                user.getCashBalance(),
                                portfolioValue,
                                totalAccountValue);
        }

        /**
         * Returns complete dashboard information for a user.
         */
        public DashboardResponse getDashboard(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<PortfolioHolding> holdings = holdingRepository.findByUser(user);

                List<Transaction> transactions = transactionRepository.findByUser(user);

                double portfolioValue = 0.0;

                for (PortfolioHolding holding : holdings) {

                        double currentPrice = marketDataService.getCurrentPrice(
                                        holding.getSymbol());

                        portfolioValue += currentPrice * holding.getQuantity();
                }

                double totalAccountValue = user.getCashBalance() + portfolioValue;

                return new DashboardResponse(
                                user.getCashBalance(),
                                portfolioValue,
                                totalAccountValue,
                                holdings,
                                transactions);
        }

}