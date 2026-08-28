package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.DishProfitabilityDto;
import com.plateprofit.ai.dto.ExpenseBreakdownDto;
import com.plateprofit.ai.dto.ProfitabilitySummaryDto;
import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.service.ProfitabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfitabilityController.class)
class ProfitabilityControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ProfitabilityService service;

    @Test
    void exposesDailyMonthlyRangeAndDetailAnalytics() throws Exception {
        ProfitabilitySummaryDto summary = new ProfitabilitySummaryDto(1L, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31),
                new BigDecimal("100"), new BigDecimal("25"), new BigDecimal("10"), new BigDecimal("65"), new BigDecimal("65"), new BigDecimal("25"));
        when(service.summary(anyLong(), any(), any())).thenReturn(summary);
        when(service.dishProfitability(anyLong(), any(), any())).thenReturn(new DishProfitabilityDto(1L, "Pasta", summary.startDate(), summary.endDate(), new BigDecimal("100"), new BigDecimal("25"), new BigDecimal("75"), new BigDecimal("75")));
        when(service.expenseBreakdown(anyLong(), any(), any())).thenReturn(List.of(new ExpenseBreakdownDto(ExpenseCategory.GAS, new BigDecimal("10"))));

        mvc.perform(get("/api/profitability/daily?restaurantId=1&date=2026-08-28")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/daily/revenue?restaurantId=1&date=2026-08-28")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/daily/expenses?restaurantId=1&date=2026-08-28")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/daily/actual-profit?restaurantId=1&date=2026-08-28")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/monthly?restaurantId=1&year=2026&month=8")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/monthly/revenue?restaurantId=1&year=2026&month=8")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/monthly/expenses?restaurantId=1&year=2026&month=8")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/monthly/actual-profit?restaurantId=1&year=2026&month=8")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/range?restaurantId=1&startDate=2026-08-01&endDate=2026-08-31")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/dishes/1?startDate=2026-08-01&endDate=2026-08-31")).andExpect(status().isOk());
        mvc.perform(get("/api/profitability/expense-breakdown?restaurantId=1&startDate=2026-08-01&endDate=2026-08-31")).andExpect(status().isOk());
    }
}
