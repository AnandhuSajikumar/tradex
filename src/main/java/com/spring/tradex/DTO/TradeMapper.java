package com.spring.tradex.DTO;

import com.spring.tradex.Enums.TradeStatus;
import com.spring.tradex.Enums.TradeType;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
public final class TradeMapper {

    public static TradeResponse toResponse(
            TradeType type,
            String symbol,
            int quantity,
            BigDecimal executionPrice,
            BigDecimal totalValue,
            BigDecimal walletBalanceAfter
    ) {
        return new TradeResponse(
                UUID.randomUUID().toString(),
                TradeStatus.SUCCESS,
                type,
                symbol,
                quantity,
                executionPrice,
                totalValue,
                walletBalanceAfter,
                Instant.now()
        );
    }
}