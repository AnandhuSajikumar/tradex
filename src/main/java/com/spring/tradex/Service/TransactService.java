package com.spring.tradex.Service;

import com.spring.tradex.DTO.TradeMapper;
import com.spring.tradex.DTO.TradeResponse;
import com.spring.tradex.Enums.TradeType;
import com.spring.tradex.Models.Trade;
import com.spring.tradex.Repositories.PortfolioRepository;
import com.spring.tradex.Repositories.StockRepository;
import com.spring.tradex.Repositories.TradeRepository;
import com.spring.tradex.Repositories.UserRepository;
import com.spring.tradex.Models.Portfolio;
import com.spring.tradex.Models.Stock;
import com.spring.tradex.Models.User;
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
    private final TradeRepository tradeRepository;

    @Transactional
    public TradeResponse buyStock(Long userId, String stockSymbol, Integer quantity){

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

        Trade trade = Trade.create(
                user,stock,TradeType.BUY,
                quantity,executionPrice
        );

        tradeRepository.save(trade);
        return TradeMapper.toResponse(trade, user.getWalletBalance());

    }

    @Transactional
    public TradeResponse sellStock(Long userId, String stockSymbol, Integer quantity){
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

        Trade trade = Trade.create(
                user, stock, TradeType.SELL,
                quantity, executionPrice
        );
        tradeRepository.save(trade);
        return TradeMapper.toResponse(trade, user.getWalletBalance());

    }
}
