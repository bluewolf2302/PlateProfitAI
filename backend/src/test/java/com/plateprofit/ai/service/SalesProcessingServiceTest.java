package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.SaleCreateDto;
import com.plateprofit.ai.dto.SaleItemRequestDto;
import com.plateprofit.ai.dto.SaleDto;
import com.plateprofit.ai.entity.*;
import com.plateprofit.ai.exception.InsufficientStockException;
import com.plateprofit.ai.repository.*;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesProcessingServiceTest {
    @Mock RestaurantRepository restaurantRepository;
    @Mock DishRepository dishRepository;
    @Mock RecipeItemRepository recipeItemRepository;
    @Mock SaleRepository saleRepository;
    @Mock SaleItemRepository saleItemRepository;
    @Mock InventoryRepository inventoryRepository;
    @Mock InventoryTransactionRepository transactionRepository;
    @InjectMocks SalesProcessingService service;

    @Test
    void processesSaleAndDeductsRequiredInventory() {
        Restaurant restaurant = restaurant(1L);
        Dish dish = dish(10L, restaurant, "20.00");
        Ingredient chicken = ingredient(100L, "Chicken", "0.08");
        Inventory stock = inventory(restaurant, chicken, "3000");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(10L)).thenReturn(Optional.of(dish));
        when(recipeItemRepository.findAllByDishId(10L)).thenReturn(List.of(recipe(dish, chicken, "250")));
        when(inventoryRepository.findByIngredientId(100L)).thenReturn(Optional.of(stock));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> { Sale sale = invocation.getArgument(0); sale.setId(50L); return sale; });

        SaleCreateDto request = new SaleCreateDto(1L, LocalDate.of(2026, 8, 28), List.of(new SaleItemRequestDto(10L, 10)));
        SaleDto result = service.processSale(request);

        assertThat(result.totalAmount()).isEqualByComparingTo("200.00");
        assertThat(stock.getCurrentQuantity()).isEqualByComparingTo("500.0000");
        verify(saleItemRepository).save(argThat(item -> item.getQuantity() == 10
                && item.getSellingPrice().compareTo(new BigDecimal("20.00")) == 0
                && item.getDishCostAtSale().compareTo(new BigDecimal("20.00")) == 0));
        verify(transactionRepository).save(argThat(transaction -> transaction.getQuantity().compareTo(new BigDecimal("2500.0000")) == 0
                && transaction.getTransactionType() == TransactionType.SALE_USAGE));
    }

    @Test
    void rejectsSaleWhenStockIsInsufficientBeforePersistingSale() {
        Restaurant restaurant = restaurant(1L);
        Dish dish = dish(10L, restaurant, "20.00");
        Ingredient chicken = ingredient(100L, "Chicken", "0.08");
        Inventory stock = inventory(restaurant, chicken, "1000");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(10L)).thenReturn(Optional.of(dish));
        when(recipeItemRepository.findAllByDishId(10L)).thenReturn(List.of(recipe(dish, chicken, "250")));
        when(inventoryRepository.findByIngredientId(100L)).thenReturn(Optional.of(stock));

        SaleCreateDto request = new SaleCreateDto(1L, LocalDate.of(2026, 8, 28), List.of(new SaleItemRequestDto(10L, 10)));

        assertThatThrownBy(() -> service.processSale(request)).isInstanceOf(InsufficientStockException.class);
        verify(saleRepository, never()).save(any());
        verify(saleItemRepository, never()).save(any());
        verify(inventoryRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
        assertThat(stock.getCurrentQuantity()).isEqualByComparingTo("1000");
    }

    private Restaurant restaurant(Long id) { Restaurant value = new Restaurant(); value.setId(id); return value; }
    private Dish dish(Long id, Restaurant restaurant, String price) { Dish value = new Dish(); value.setId(id); value.setRestaurant(restaurant); value.setSellingPrice(new BigDecimal(price)); value.setName("Biryani"); return value; }
    private Ingredient ingredient(Long id, String name, String cost) { Ingredient value = new Ingredient(); value.setId(id); value.setName(name); value.setCostPerUnit(new BigDecimal(cost)); return value; }
    private Inventory inventory(Restaurant restaurant, Ingredient ingredient, String quantity) { Inventory value = new Inventory(); value.setRestaurant(restaurant); value.setIngredient(ingredient); value.setCurrentQuantity(new BigDecimal(quantity)); return value; }
    private RecipeItem recipe(Dish dish, Ingredient ingredient, String quantity) { RecipeItem value = new RecipeItem(); value.setId(1L); value.setDish(dish); value.setIngredient(ingredient); value.setQuantityRequired(new BigDecimal(quantity)); return value; }
}
