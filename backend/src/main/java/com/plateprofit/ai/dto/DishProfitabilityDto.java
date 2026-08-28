package com.plateprofit.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DishProfitabilityDto(
        Long dishId,
        String dishName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal revenue,
        BigDecimal dishCosts,
        BigDecimal grossProfit,
        BigDecimal profitMarginPercentage) {
}
