package com.plateprofit.ai.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record RecipeItemDto(Long id, @NotNull Long dishId, @NotNull Long ingredientId, @NotNull @Positive BigDecimal quantityRequired) { }
