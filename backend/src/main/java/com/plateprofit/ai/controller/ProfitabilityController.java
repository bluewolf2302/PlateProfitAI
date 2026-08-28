package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.DishProfitabilityDto;
import com.plateprofit.ai.dto.ExpenseBreakdownDto;
import com.plateprofit.ai.dto.ProfitabilitySummaryDto;
import com.plateprofit.ai.service.ProfitabilityService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/profitability")
public class ProfitabilityController {
    private final ProfitabilityService service;

    public ProfitabilityController(ProfitabilityService service) {
        this.service = service;
    }

    @GetMapping("/daily")
    public ProfitabilitySummaryDto daily(@RequestParam Long restaurantId, @RequestParam LocalDate date) {
        return service.summary(restaurantId, date, date);
    }

    @GetMapping("/daily/revenue")
    public BigDecimal dailyRevenue(@RequestParam Long restaurantId, @RequestParam LocalDate date) {
        return service.summary(restaurantId, date, date).totalRevenue();
    }

    @GetMapping("/daily/expenses")
    public BigDecimal dailyExpenses(@RequestParam Long restaurantId, @RequestParam LocalDate date) {
        return service.summary(restaurantId, date, date).totalExpenses();
    }

    @GetMapping("/daily/actual-profit")
    public BigDecimal dailyActualProfit(@RequestParam Long restaurantId, @RequestParam LocalDate date) {
        return service.summary(restaurantId, date, date).actualProfit();
    }

    @GetMapping("/monthly")
    public ProfitabilitySummaryDto monthly(@RequestParam Long restaurantId, @RequestParam int year, @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        return service.summary(restaurantId, start, start.withDayOfMonth(start.lengthOfMonth()));
    }

    @GetMapping("/monthly/revenue")
    public BigDecimal monthlyRevenue(@RequestParam Long restaurantId, @RequestParam int year, @RequestParam int month) {
        return monthly(restaurantId, year, month).totalRevenue();
    }

    @GetMapping("/monthly/expenses")
    public BigDecimal monthlyExpenses(@RequestParam Long restaurantId, @RequestParam int year, @RequestParam int month) {
        return monthly(restaurantId, year, month).totalExpenses();
    }

    @GetMapping("/monthly/actual-profit")
    public BigDecimal monthlyActualProfit(@RequestParam Long restaurantId, @RequestParam int year, @RequestParam int month) {
        return monthly(restaurantId, year, month).actualProfit();
    }

    @GetMapping("/range")
    public ProfitabilitySummaryDto range(@RequestParam Long restaurantId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return service.summary(restaurantId, startDate, endDate);
    }

    @GetMapping("/dishes/{dishId}")
    public DishProfitabilityDto dish(@PathVariable Long dishId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return service.dishProfitability(dishId, startDate, endDate);
    }

    @GetMapping("/expense-breakdown")
    public List<ExpenseBreakdownDto> expenseBreakdown(@RequestParam Long restaurantId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return service.expenseBreakdown(restaurantId, startDate, endDate);
    }
}
