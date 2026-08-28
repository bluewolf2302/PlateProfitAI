package com.plateprofit.ai.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleItemRequestDto(
        @NotNull Long dishId,
        @NotNull @Positive Integer quantity) {
}
