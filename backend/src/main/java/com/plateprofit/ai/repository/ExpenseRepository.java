package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpenseRepository extends JpaRepository<Expense, Long> { }
