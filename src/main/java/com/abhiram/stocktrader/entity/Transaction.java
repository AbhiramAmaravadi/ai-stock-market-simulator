package com.abhiram.stocktrader.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private Integer quantity;

    private Double price;

    private String type;

    private LocalDateTime timestamp;
    /**
     * Owner of the transaction.
     * Hidden from API responses to prevent recursive serialization.
     */
    @ManyToOne
    @JsonIgnore
    private User user;
}