package com.spring.tradex.components;



import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class MarketHoursValidator {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");
    private static final LocalTime OPEN = LocalTime.of(9,15);
    private static final LocalTime CLOSE = LocalTime.of(3,15);

    public void validateMarketHours() {
        ZonedDateTime now = ZonedDateTime.now();
        DayOfWeek day = now.getDayOfWeek();

        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            throw new IllegalStateException("Market is closed on weekends");
        }

        LocalTime time = LocalTime.now();
        if(time.isBefore(OPEN) || time.isAfter(CLOSE)){
            throw new IllegalStateException("Market is closed");
        }
    }
}
