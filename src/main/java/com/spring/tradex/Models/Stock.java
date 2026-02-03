package com.spring.tradex.Models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false,unique = true)
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @PreUpdate
    public void onUpdate(){
        this.localDateTime = LocalDateTime.now();
    }

    public Stock(String symbol, String companyName, BigDecimal currentPrice,
                 LocalDateTime localDateTime) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.localDateTime = localDateTime;
    }

    public void updatePrice(BigDecimal newPrice){
        if(newPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.currentPrice = newPrice;
    }













    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && Objects.equals(companyName, stock.companyName) && Objects.equals(currentPrice, stock.currentPrice) && Objects.equals(localDateTime, stock.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, currentPrice, localDateTime);
    }
}
