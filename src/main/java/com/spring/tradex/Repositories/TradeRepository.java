package com.spring.tradex.Repositories;

import com.spring.tradex.Models.Trade;
import com.spring.tradex.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    Page<Trade> findByUserIdOrderByExecutedAtDesc(long userId, Pageable pageable);

    long user(User user);
}
