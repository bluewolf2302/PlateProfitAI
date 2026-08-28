package com.plateprofit.ai.dto;

import java.math.BigDecimal;

public record DishCostDto(
        Long dishId,
        String dishName,
        BigDecimal sellingPrice,
        BigDecimal dishCost,
        BigDecimal grossProfit,
        BigDecimal profitMarginPercentage) {
}
