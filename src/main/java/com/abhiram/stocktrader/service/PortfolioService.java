package com.abhiram.stocktrader.service;

import com.abhiram.stocktrader.dto.BuyStockRequest;
import com.abhiram.stocktrader.dto.DashboardResponse;
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
import com.abhiram.stocktrader.dto.DashboardResponse;

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
                                                                .averagePrice(currentPrice)
                                                                .build());

                holding.setQuantity(
                                holding.getQuantity()
                                                + request.getQuantity());

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

        public List<PortfolioHolding> getPortfolio(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return holdingRepository.findByUser(user);
        }

        public List<Transaction> getTransactions(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return transactionRepository.findByUser(user);
        }

        public void sellStock(SellStockRequest request) {

                User user = userRepository.findByEmail(
                                request.getEmail())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                PortfolioHolding holding = holdingRepository.findByUserAndSymbol(
                                user,
                                request.getSymbol())
                                .orElseThrow(() -> new RuntimeException(
                                                "Stock not owned"));

                if (holding.getQuantity() < request.getQuantity()) {

                        throw new RuntimeException(
                                        "Not enough shares");
                }

                Double currentPrice = marketDataService.getCurrentPrice(
                                request.getSymbol());

                Double saleValue = currentPrice * request.getQuantity();

                user.setCashBalance(
                                user.getCashBalance() + saleValue);

                userRepository.save(user);

                holding.setQuantity(
                                holding.getQuantity()
                                                - request.getQuantity());

                if (holding.getQuantity() == 0) {
                        holdingRepository.delete(holding);
                } else {
                        holdingRepository.save(holding);
                }

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