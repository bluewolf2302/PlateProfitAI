package com.plateprofit.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfitabilitySummaryDto(
        Long restaurantId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalRevenue,
        BigDecimal totalDishCosts,
        BigDecimal totalExpenses,
        BigDecimal actualProfit,
        BigDecimal profitMarginPercentage,
        BigDecimal foodCostPercentage) {
}
