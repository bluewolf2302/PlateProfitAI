package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.ExpenseDto;
import com.plateprofit.ai.entity.Expense;
import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.entity.Restaurant;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.ExpenseRepository;
import com.plateprofit.ai.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final RestaurantRepository restaurantRepository;

    public ExpenseService(ExpenseRepository expenseRepository, RestaurantRepository restaurantRepository) {
        this.expenseRepository = expenseRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public ExpenseDto create(ExpenseDto request) {
        Expense expense = new Expense();
        apply(expense, request);
        return toDto(expenseRepository.save(expense));
    }

    public ExpenseDto update(Long id, ExpenseDto request) {
        Expense expense = find(id);
        apply(expense, request);
        return toDto(expense);
    }

    @Transactional(readOnly = true)
    public ExpenseDto get(Long id) {
        return toDto(find(id));
    }

    @Transactional(readOnly = true)
    public List<ExpenseDto> find(Long restaurantId, LocalDate startDate, LocalDate endDate, ExpenseCategory category) {
        List<Expense> expenses = category == null
                ? expenseRepository.findAllByRestaurantIdAndExpenseDateBetween(restaurantId, startDate, endDate)
                : expenseRepository.findAllByRestaurantIdAndCategoryAndExpenseDateBetween(restaurantId, category, startDate, endDate);
        return expenses.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal total(Long restaurantId, LocalDate startDate, LocalDate endDate, ExpenseCategory category) {
        BigDecimal total = category == null
                ? expenseRepository.sumAmountByRestaurantIdAndExpenseDateBetween(restaurantId, startDate, endDate)
                : expenseRepository.sumAmountByRestaurantIdAndCategoryAndExpenseDateBetween(restaurantId, category, startDate, endDate);
        return total == null ? BigDecimal.ZERO : total;
    }

    public void delete(Long id) {
        expenseRepository.delete(find(id));
    }

    private Expense find(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with id " + id + " was not found"));
    }

    private void apply(Expense expense, ExpenseDto request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + request.restaurantId() + " was not found"));
        expense.setRestaurant(restaurant);
        expense.setCategory(request.category());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setExpenseDate(request.expenseDate());
        expense.setBillFileUrl(request.billFileUrl());
    }

    private ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(expense.getId(), expense.getRestaurant().getId(), expense.getCategory(),
                expense.getDescription(), expense.getAmount(), expense.getExpenseDate(), expense.getBillFileUrl());
    }
}
