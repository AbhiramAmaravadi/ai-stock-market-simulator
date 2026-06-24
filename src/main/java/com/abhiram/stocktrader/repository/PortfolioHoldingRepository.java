package com.abhiram.stocktrader.repository;

import com.abhiram.stocktrader.entity.PortfolioHolding;
import com.abhiram.stocktrader.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioHoldingRepository
        extends JpaRepository<PortfolioHolding, Long> {

    List<PortfolioHolding> findByUser(User user);

    Optional<PortfolioHolding> findByUserAndSymbol(User user, String symbol);
}