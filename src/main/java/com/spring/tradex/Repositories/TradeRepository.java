package com.spring.tradex.Repositories;

import com.spring.tradex.Models.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
