package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.*;
import com.plateprofit.ai.entity.*;
import com.plateprofit.ai.exception.GlobalExceptionHandler;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.service.ResourceCrudService;
import com.plateprofit.ai.service.DishCostingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RestaurantController.class, DishController.class, IngredientController.class,
        RecipeController.class, SaleController.class, InventoryController.class, ExpenseController.class})
@Import(GlobalExceptionHandler.class)
class CrudControllerTest {
    @Autowired MockMvc mvc;
    @MockBean ResourceCrudService service;
    @MockBean DishCostingService costingService;

    @Test
    void restaurantCrudEndpointsWork() throws Exception {
        when(service.restaurants()).thenReturn(List.of());
        exerciseCrud("/api/restaurants", "{\"name\":\"Cafe\"}");
    }

    @Test
    void dishCrudEndpointsWork() throws Exception {
        when(service.dishes()).thenReturn(List.of());
        exerciseCrud("/api/dishes", "{\"restaurantId\":1,\"name\":\"Pasta\",\"sellingPrice\":12.50,\"active\":true}");
    }

    @Test
    void dishCostEndpointIsAvailable() throws Exception {
        mvc.perform(get("/api/dishes/1/cost")).andExpect(status().isOk());
    }

    @Test
    void ingredientCrudEndpointsWork() throws Exception {
        when(service.ingredients()).thenReturn(List.of());
        exerciseCrud("/api/ingredients", "{\"restaurantId\":1,\"name\":\"Tomato\",\"unit\":\"GRAM\",\"costPerUnit\":0.10,\"currentStock\":2,\"minimumStock\":1}");
    }

    @Test
    void recipeCrudEndpointsWork() throws Exception {
        when(service.recipes()).thenReturn(List.of());
        exerciseCrud("/api/recipes", "{\"dishId\":1,\"ingredientId\":1,\"quantityRequired\":100}");
    }

    @Test
    void saleCrudEndpointsWork() throws Exception {
        when(service.sales()).thenReturn(List.of());
        exerciseCrud("/api/sales", "{\"restaurantId\":1,\"saleDate\":\"2026-08-28\",\"totalAmount\":25}");
    }

    @Test
    void inventoryCrudEndpointsWork() throws Exception {
        when(service.inventory()).thenReturn(List.of());
        exerciseCrud("/api/inventory", "{\"restaurantId\":1,\"ingredientId\":1,\"currentQuantity\":5}");
    }

    @Test
    void expenseCrudEndpointsWork() throws Exception {
        when(service.expenses()).thenReturn(List.of());
        exerciseCrud("/api/expenses", "{\"restaurantId\":1,\"category\":\"GAS\",\"amount\":50,\"expenseDate\":\"2026-08-28\"}");
    }

    @Test
    void invalidRequestReturnsBadRequest() throws Exception {
        mvc.perform(post("/api/restaurants").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingResourceReturnsNotFound() throws Exception {
        when(service.restaurant(999L)).thenThrow(new ResourceNotFoundException("Restaurant with id 999 was not found"));
        mvc.perform(get("/api/restaurants/999")).andExpect(status().isNotFound());
    }

    private void exerciseCrud(String path, String json) throws Exception {
        mvc.perform(get(path)).andExpect(status().isOk());
        mvc.perform(get(path + "/1")).andExpect(status().isOk());
        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
        mvc.perform(put(path + "/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
        mvc.perform(delete(path + "/1")).andExpect(status().isNoContent());
    }
}
