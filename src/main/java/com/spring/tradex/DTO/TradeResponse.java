package com.spring.tradex.DTO;

import com.spring.tradex.Enums.TradeStatus;
import com.spring.tradex.Enums.TradeType;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class TradeResponse {

    private final String tradeId;
    private final TradeStatus status;
    private final TradeType type;
    private final String symbol;
    private final int quantity;
    private final BigDecimal priceExecuted;
    private final BigDecimal totalValue;
    private final BigDecimal remainingWalletBalance;
    private final Instant timestamp;

    public TradeResponse(
            String tradeId,
            TradeStatus status,
            TradeType type,
            String symbol,
            int quantity,
            BigDecimal priceExecuted,
            BigDecimal totalValue,
            BigDecimal remainingWalletBalance,
            Instant timestamp
    ) {
        this.tradeId = tradeId;
        this.status = status;
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.priceExecuted = priceExecuted;
        this.totalValue = totalValue;
        this.remainingWalletBalance = remainingWalletBalance;
        this.timestamp = timestamp;
    }
}
