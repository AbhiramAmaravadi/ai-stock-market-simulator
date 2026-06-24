package com.abhiram.stocktrader.repository;

import com.abhiram.stocktrader.entity.Transaction;
import com.abhiram.stocktrader.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);
}