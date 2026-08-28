package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.ExpenseDto;
import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.entity.Restaurant;
import com.plateprofit.ai.repository.ExpenseRepository;
import com.plateprofit.ai.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock ExpenseRepository expenseRepository;
    @Mock RestaurantRepository restaurantRepository;
    @InjectMocks ExpenseService service;

    @Test
    void filtersAndTotalsExpensesByDateAndCategory() {
        LocalDate start = LocalDate.of(2026, 8, 1);
        LocalDate end = LocalDate.of(2026, 8, 31);
        when(expenseRepository.findAllByRestaurantIdAndCategoryAndExpenseDateBetween(1L, ExpenseCategory.GAS, start, end)).thenReturn(List.of());
        when(expenseRepository.sumAmountByRestaurantIdAndCategoryAndExpenseDateBetween(1L, ExpenseCategory.GAS, start, end)).thenReturn(new BigDecimal("125.50"));

        assertThat(service.find(1L, start, end, ExpenseCategory.GAS)).isEmpty();
        assertThat(service.total(1L, start, end, ExpenseCategory.GAS)).isEqualByComparingTo("125.50");
    }

    @Test
    void createsExpenseWithRestaurantReferenceAndStoragePath() {
        Restaurant restaurant = new Restaurant(); restaurant.setId(1L);
        ExpenseDto request = new ExpenseDto(null, 1L, ExpenseCategory.FOOD, "Invoice", new BigDecimal("50"), LocalDate.of(2026, 8, 28), "receipts/food.pdf");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(expenseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ExpenseDto result = service.create(request);

        assertThat(result.billFileUrl()).isEqualTo("receipts/food.pdf");
        assertThat(result.category()).isEqualTo(ExpenseCategory.FOOD);
    }
}
