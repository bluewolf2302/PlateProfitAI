package com.plateprofit.ai.dto;

import com.plateprofit.ai.entity.ExpenseCategory;
import java.math.BigDecimal;

public record ExpenseBreakdownDto(ExpenseCategory category, BigDecimal amount) {
}
