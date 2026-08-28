package com.plateprofit.ai.dto;

import java.math.BigDecimal;

public record SaleItemDto(
        Long id,
        Long dishId,
        Integer quantity,
        BigDecimal sellingPrice,
        BigDecimal dishCostAtSale) {
}
