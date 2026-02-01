package com.spring.tradex.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "portfolio")
@Getter
@NoArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal avgBuyPrice;

    public Portfolio(User user, Stock stock) {
        this.user = user;
        this.stock = stock;
        this.quantity = 0;
        this.avgBuyPrice = BigDecimal.ZERO;
    }



    public static Portfolio createEmptyPortfolio(User user, Stock stock){
        if(user == null || stock == null){
            throw new IllegalArgumentException("User and Stock cannot be null");
        }
        return new Portfolio(user, stock);
    }

    public void addHoldings(Integer qtyToAdd, BigDecimal buyPrice){
        if(qtyToAdd <= 0) throw new IllegalArgumentException("Quantity must be positive");

        if(buyPrice == null || buyPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Buy price must be positive");
        }

        BigDecimal totalValue = this.avgBuyPrice.multiply(BigDecimal.valueOf(this.quantity))
                .add(buyPrice.multiply(BigDecimal.valueOf(qtyToAdd)));

        this.quantity = Math.addExact(this.quantity, qtyToAdd);
        this.avgBuyPrice = totalValue.divide(BigDecimal.valueOf(this.quantity),
                2,RoundingMode.HALF_UP);
    }

    public void removeHoldings(Integer qtyToRemove){
        if(qtyToRemove <= 0) throw new IllegalArgumentException("Quantity must be positive");

        if(this.quantity < qtyToRemove) throw new IllegalStateException("Insufficient holdings to sell");

        this.quantity = Math.subtractExact(this.quantity, qtyToRemove);
    }
}
