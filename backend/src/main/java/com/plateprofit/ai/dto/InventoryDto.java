package com.plateprofit.ai.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record InventoryDto(Long id, @NotNull Long restaurantId, @NotNull Long ingredientId, @NotNull @PositiveOrZero BigDecimal currentQuantity) { }
