package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import com.plateprofit.ai.entity.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findAllByRestaurantIdAndExpenseDateBetween(Long restaurantId, LocalDate startDate, LocalDate endDate);
	List<Expense> findAllByRestaurantIdAndCategoryAndExpenseDateBetween(Long restaurantId, ExpenseCategory category, LocalDate startDate, LocalDate endDate);
	long countByRestaurantIdAndExpenseDateBetween(Long restaurantId, LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("select coalesce(sum(e.amount), 0) from Expense e where e.restaurant.id = :restaurantId and e.expenseDate between :startDate and :endDate")
	BigDecimal sumAmountByRestaurantIdAndExpenseDateBetween(Long restaurantId, LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("select coalesce(sum(e.amount), 0) from Expense e where e.restaurant.id = :restaurantId and e.category = :category and e.expenseDate between :startDate and :endDate")
	BigDecimal sumAmountByRestaurantIdAndCategoryAndExpenseDateBetween(Long restaurantId, ExpenseCategory category, LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("select e.category, coalesce(sum(e.amount), 0) from Expense e where e.restaurant.id = :restaurantId and e.expenseDate between :startDate and :endDate group by e.category")
	List<Object[]> sumByCategory(Long restaurantId, LocalDate startDate, LocalDate endDate);
}
