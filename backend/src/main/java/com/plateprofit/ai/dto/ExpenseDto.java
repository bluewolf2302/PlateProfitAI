package com.plateprofit.ai.dto;

import com.plateprofit.ai.entity.ExpenseCategory;
import jakarta.validation.constraints.*;
import java.math.BigDecimal; import java.time.LocalDate;
public record ExpenseDto(Long id, @NotNull Long restaurantId, @NotNull ExpenseCategory category, String description, @NotNull @Positive BigDecimal amount, @NotNull LocalDate expenseDate, String billFileUrl) { }
