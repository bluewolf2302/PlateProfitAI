package com.plateprofit.ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record SaleCreateDto(
        @NotNull Long restaurantId,
        @NotNull LocalDate saleDate,
        @NotEmpty List<@Valid SaleItemRequestDto> items) {
}
