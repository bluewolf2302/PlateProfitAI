package com.plateprofit.ai.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal; import java.time.LocalDate;
public record SaleDto(Long id, @NotNull Long restaurantId, @NotNull LocalDate saleDate, @NotNull @PositiveOrZero BigDecimal totalAmount) { }
