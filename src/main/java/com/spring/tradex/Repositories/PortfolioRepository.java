package com.spring.tradex.Repositories;

import com.spring.tradex.models.Portfolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long Id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId AND p.stock.id = :stockId")
    Optional<Portfolio> findByUserIdAndStockIdWithLock(@Param("userId" )long userId, @Param("stockId") Long stockId);

    Optional<Portfolio> findByUserIdAndStockId(long userId, Long stockId);
}
