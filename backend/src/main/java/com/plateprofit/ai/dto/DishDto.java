package com.plateprofit.ai.dto;

import com.plateprofit.ai.entity.Dish;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record DishDto(Long id, Long restaurantId, @NotBlank String name, String description, String category, @NotNull @Positive BigDecimal sellingPrice, boolean active) { }
