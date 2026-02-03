package com.spring.tradex.Repositories;

import com.spring.tradex.Service.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
