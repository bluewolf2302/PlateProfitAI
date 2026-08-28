package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.*;
import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.entity.Dish;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProfitabilityService {
    private static final int MONEY_SCALE = 2;
    private static final int PERCENTAGE_SCALE = 4;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ExpenseRepository expenseRepository;
    private final DishRepository dishRepository;

    public ProfitabilityService(SaleRepository saleRepository, SaleItemRepository saleItemRepository,
                                ExpenseRepository expenseRepository, DishRepository dishRepository) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.expenseRepository = expenseRepository;
        this.dishRepository = dishRepository;
    }

    public ProfitabilitySummaryDto summary(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);
        BigDecimal revenue = money(saleRepository.sumRevenue(restaurantId, startDate, endDate));
        BigDecimal dishCosts = money(saleItemRepository.sumDishCosts(restaurantId, startDate, endDate));
        BigDecimal expenses = money(expenseRepository.sumAmountByRestaurantIdAndExpenseDateBetween(restaurantId, startDate, endDate));
        BigDecimal actualProfit = revenue.subtract(dishCosts).subtract(expenses).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        return new ProfitabilitySummaryDto(restaurantId, startDate, endDate, revenue, dishCosts, expenses, actualProfit,
                percentage(actualProfit, revenue), percentage(dishCosts, revenue));
    }

    public List<ExpenseBreakdownDto> expenseBreakdown(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);
        List<ExpenseBreakdownDto> breakdown = expenseRepository.sumByCategory(restaurantId, startDate, endDate).stream()
                .map(row -> new ExpenseBreakdownDto((ExpenseCategory) row[0], money((BigDecimal) row[1])))
                .toList();
        return addMissingCategories(breakdown);
    }

    public DishProfitabilityDto dishProfitability(Long dishId, LocalDate startDate, LocalDate endDate) {
        validateRange(startDate, endDate);
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish with id " + dishId + " was not found"));
        BigDecimal revenue = money(saleItemRepository.sumDishRevenue(dishId, startDate, endDate));
        BigDecimal costs = money(saleItemRepository.sumDishCost(dishId, startDate, endDate));
        BigDecimal grossProfit = revenue.subtract(costs).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        return new DishProfitabilityDto(dishId, dish.getName(), startDate, endDate, revenue, costs, grossProfit, percentage(grossProfit, revenue));
    }

    private List<ExpenseBreakdownDto> addMissingCategories(List<ExpenseBreakdownDto> breakdown) {
        return Arrays.stream(ExpenseCategory.values()).map(category -> breakdown.stream()
                .filter(item -> item.category() == category)
                .findFirst().orElse(new ExpenseBreakdownDto(category, BigDecimal.ZERO.setScale(MONEY_SCALE)))
        ).toList();
    }

    private BigDecimal money(BigDecimal value) { return (value == null ? BigDecimal.ZERO : value).setScale(MONEY_SCALE, RoundingMode.HALF_UP); }
    private BigDecimal percentage(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.signum() == 0) return BigDecimal.ZERO.setScale(PERCENTAGE_SCALE);
        return numerator.multiply(BigDecimal.valueOf(100)).divide(denominator, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }
    private void validateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) throw new IllegalArgumentException("End date must not be before start date");
    }
}
