package com.plateprofit.ai.service;

import com.plateprofit.ai.entity.Dish;
import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.repository.DishRepository;
import com.plateprofit.ai.repository.ExpenseRepository;
import com.plateprofit.ai.repository.SaleItemRepository;
import com.plateprofit.ai.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfitabilityServiceTest {
    @Mock SaleRepository saleRepository;
    @Mock SaleItemRepository saleItemRepository;
    @Mock ExpenseRepository expenseRepository;
    @Mock DishRepository dishRepository;
    @InjectMocks ProfitabilityService service;

    @Test
    void calculatesActualProfitAndPercentages() {
        LocalDate date = LocalDate.of(2026, 8, 28);
        when(saleRepository.sumRevenue(1L, date, date)).thenReturn(new BigDecimal("1000"));
        when(saleItemRepository.sumDishCosts(1L, date, date)).thenReturn(new BigDecimal("300"));
        when(expenseRepository.sumAmountByRestaurantIdAndExpenseDateBetween(1L, date, date)).thenReturn(new BigDecimal("200"));

        var result = service.summary(1L, date, date);

        assertThat(result.actualProfit()).isEqualByComparingTo("500.00");
        assertThat(result.profitMarginPercentage()).isEqualByComparingTo("50.0000");
        assertThat(result.foodCostPercentage()).isEqualByComparingTo("30.0000");
    }

    @Test
    void handlesZeroRevenueWithoutDivisionErrors() {
        LocalDate date = LocalDate.of(2026, 8, 28);
        when(saleRepository.sumRevenue(1L, date, date)).thenReturn(null);
        when(saleItemRepository.sumDishCosts(1L, date, date)).thenReturn(null);
        when(expenseRepository.sumAmountByRestaurantIdAndExpenseDateBetween(1L, date, date)).thenReturn(new BigDecimal("25"));

        var result = service.summary(1L, date, date);

        assertThat(result.actualProfit()).isEqualByComparingTo("-25.00");
        assertThat(result.profitMarginPercentage()).isEqualByComparingTo("0.0000");
        assertThat(result.foodCostPercentage()).isEqualByComparingTo("0.0000");
    }

    @Test
    void calculatesDishProfitabilityFromHistoricalSaleCosts() {
        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 31);
        Dish dish = new Dish(); dish.setId(5L); dish.setName("Pasta");
        when(dishRepository.findById(5L)).thenReturn(Optional.of(dish));
        when(saleItemRepository.sumDishRevenue(5L, start, end)).thenReturn(new BigDecimal("200"));
        when(saleItemRepository.sumDishCost(5L, start, end)).thenReturn(new BigDecimal("80"));

        var result = service.dishProfitability(5L, start, end);

        assertThat(result.grossProfit()).isEqualByComparingTo("120.00");
        assertThat(result.profitMarginPercentage()).isEqualByComparingTo("60.0000");
    }

    @Test
    void returnsAllExpenseCategoriesInBreakdown() {
        LocalDate date = LocalDate.of(2026, 8, 28);
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{ExpenseCategory.GAS, new BigDecimal("25")});
        when(expenseRepository.sumByCategory(1L, date, date)).thenReturn(rows);

        var result = service.expenseBreakdown(1L, date, date);

        assertThat(result).hasSize(ExpenseCategory.values().length);
        assertThat(result.stream().filter(item -> item.category() == ExpenseCategory.GAS).findFirst().orElseThrow().amount()).isEqualByComparingTo("25.00");
    }
}
