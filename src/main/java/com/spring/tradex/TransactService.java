package com.spring.tradex;

import com.spring.tradex.Repositories.PortfolioRepository;
import com.spring.tradex.Repositories.StockRepository;
import com.spring.tradex.Repositories.UserRepository;
import com.spring.tradex.models.Portfolio;
import com.spring.tradex.models.Stock;
import com.spring.tradex.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;

    @Transactional
    public void buyStock(Long userId, String stockSymbol, Integer quantity){

        if(quantity <= 0 ) throw new IllegalArgumentException("Quantity must be positive");

        User user =  userRepository.findByIdWithLock(userId).
                orElseThrow(() -> new IllegalStateException("User not found"));
        Stock stock = stockRepository.findBySymbol(stockSymbol)
                .orElseThrow(() -> new IllegalStateException("Stock not found"));

        BigDecimal executionPrice = stock.getCurrentPrice();
        BigDecimal totalCost = executionPrice.multiply(BigDecimal.valueOf(quantity));

        user.debitWallet(totalCost);

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndStockIdWithLock(userId, stock.getId())
                .orElse(Portfolio.createEmptyPortfolio(user, stock));

        portfolio.addHoldings(quantity, executionPrice);
        portfolioRepository.save(portfolio);
    }

    public void sellStock(Long userId, String stockSymbol, Integer quantity){
        if(quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        User user  =  userRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        Stock stock  = stockRepository.findBySymbol(stockSymbol)
                .orElseThrow(() -> new IllegalStateException("Stock not found"));

        BigDecimal executionPrice = stock.getCurrentPrice();

        Portfolio portfolio = portfolioRepository
                .findByUserIdAndStockIdWithLock(userId, stock.getId())
                .orElseThrow(() -> new IllegalStateException("You do not own this portfolio"));

        portfolio.removeHoldings(quantity);

        BigDecimal totalValue = executionPrice.multiply(BigDecimal.valueOf(quantity));

        user.creditWallet(totalValue);
        userRepository.save(user);

    }
}
