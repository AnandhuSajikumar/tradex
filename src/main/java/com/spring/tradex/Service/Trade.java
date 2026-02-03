package com.spring.tradex.Service;

import com.spring.tradex.Enums.TradeType;
import com.spring.tradex.Models.Stock;
import com.spring.tradex.Models.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceExecuted;

    @Column(nullable = false)
    private Instant executedAt;

    public Trade(User user, Stock stock, TradeType tradeType,
                 int quantity, BigDecimal priceExecuted,
                 Instant executedAt) {

        this.user = user;
        this.stock = stock;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.priceExecuted = priceExecuted;
        this.executedAt = executedAt;
    }
}
