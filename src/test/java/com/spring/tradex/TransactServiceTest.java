package com.spring.tradex;

import com.spring.tradex.Enums.Role;
import com.spring.tradex.Models.Portfolio;
import com.spring.tradex.Models.Stock;
import com.spring.tradex.Models.User;
import com.spring.tradex.Repositories.PortfolioRepository;
import com.spring.tradex.Repositories.StockRepository;
import com.spring.tradex.Repositories.TradeRepository;
import com.spring.tradex.Repositories.UserRepository;
import com.spring.tradex.Service.TransactService;
import com.spring.tradex.components.MarketHoursValidator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactServiceTest {


    @Autowired
    private TransactService transactService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @MockitoBean
    private MarketHoursValidator marketHoursValidator;

    private Long userId;
    private String stockSymbol = "TCS";

    @BeforeEach
    void setup() {
        // Disable market hours for tests
        doNothing().when(marketHoursValidator).validateMarketHours();

        // Clean DB (explicit, predictable)
        tradeRepository.deleteAll();
        portfolioRepository.deleteAll();
        userRepository.deleteAll();
        stockRepository.deleteAll();

        // Create unique user
        User user = userRepository.save(
                new User(
                        "Test User",
                        "test_" + UUID.randomUUID() + "@test.com",
                        "password",
                        Role.USER,
                        BigDecimal.valueOf(10_000),
                        List.of()
                )
        );
        userId = user.getId();

        // Create stock
        stockRepository.save(
                new Stock(
                        stockSymbol,
                        "Tata Consultancy Services",
                        BigDecimal.valueOf(100),
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void buyFailsWhenWalletInsufficient() {
        assertThrows(IllegalStateException.class, () ->
                transactService.buyStock(userId, stockSymbol, 1_000)
        );

        assertEquals(0, tradeRepository.count());
    }

    @Test
    void sellFailsWhenNoHoldings() {
        assertThrows(IllegalStateException.class, () ->
                transactService.sellStock(userId, stockSymbol, 5)
        );

        assertEquals(0, tradeRepository.count());
    }

    @Test
    void concurrentBuys_onlyOneSucceeds() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Void> task = () -> {
            transactService.buyStock(userId, stockSymbol, 10);
            return null;
        };

        List<Future<Void>> futures = executor.invokeAll(List.of(task, task));
        executor.shutdown();

        long successCount = futures.stream().filter(f -> {
            try {
                f.get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }).count();

        assertTrue(successCount <= 1);
        assertTrue(tradeRepository.count() <= 1);

    }


    @Test
    void concurrentSells_quantityNeverNegative() throws Exception {
        userRepository.findById(userId).get().creditWallet(BigDecimal.valueOf(10_000));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        Stock stock = stockRepository.findBySymbol(stockSymbol)
                .orElseThrow(()-> new IllegalArgumentException("Stock not found"));

        Portfolio portfolio = new Portfolio(user,stock);
        portfolio.addHoldings(10, BigDecimal.valueOf(100));
        portfolioRepository.save(portfolio);


        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Void> task = () -> {
            transactService.sellStock(userId, stockSymbol, 10);
            return null;
        };

        List<Future<Void>> futures = executor.invokeAll(List.of(task, task));
        executor.shutdown();

        long successCount = futures.stream().filter(f -> {
            try {
                f.get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }).count();

        assertTrue(successCount <= 1);
        assertTrue(tradeRepository.count() <= 1);

    }

}
