package com.spring.tradex.Models;

import com.spring.tradex.Enums.TradeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "trades",
        indexes = {
                @Index(name = "idx_trade_user_time", columnList = "user_id, executedAt"),
                @Index(name = "idx_trade_stock_time", columnList = "stock_id, executedAt")
        }
)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
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

    private Trade(User user, Stock stock,
                 TradeType tradeType,
                 int quantity, BigDecimal priceExecuted) {

        this.user = user;
        this.stock = stock;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.priceExecuted = priceExecuted;
        this.executedAt = Instant.now();
    }

    public static Trade create(
            User user,
            Stock stock,
            TradeType tradeType,
            int quantity,
            BigDecimal executionPrice
    ){
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if(executionPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Execution price should be zero");
        }
        return new Trade(user, stock, tradeType, quantity, executionPrice);
    }

}
