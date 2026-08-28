package com.plateprofit.ai.dto;

import com.plateprofit.ai.entity.Unit;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record IngredientDto(Long id, Long restaurantId, @NotBlank String name, @NotNull Unit unit, @NotNull @PositiveOrZero BigDecimal costPerUnit, @NotNull @PositiveOrZero BigDecimal currentStock, @NotNull @PositiveOrZero BigDecimal minimumStock) { }
