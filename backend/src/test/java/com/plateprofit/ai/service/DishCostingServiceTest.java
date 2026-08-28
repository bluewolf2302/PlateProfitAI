package com.plateprofit.ai.service;

import com.plateprofit.ai.entity.Dish;
import com.plateprofit.ai.entity.Ingredient;
import com.plateprofit.ai.entity.RecipeItem;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.DishRepository;
import com.plateprofit.ai.repository.RecipeItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishCostingServiceTest {
    @Mock DishRepository dishRepository;
    @Mock RecipeItemRepository recipeItemRepository;
    @InjectMocks DishCostingService service;

    @Test
    void calculatesCostProfitAndMarginUsingBigDecimal() {
        Dish dish = dish("30.00");
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(recipeItemRepository.findAllByDishId(1L)).thenReturn(List.of(
                recipeItem(dish, ingredient("0.12"), "100"),
                recipeItem(dish, ingredient("0.05"), "50")));

        var result = service.calculateCost(1L);

        assertThat(result.dishCost()).isEqualByComparingTo("14.50");
        assertThat(result.grossProfit()).isEqualByComparingTo("15.50");
        assertThat(result.profitMarginPercentage()).isEqualByComparingTo("51.6667");
    }

    @Test
    void returnsZeroCostForDishWithoutRecipeItems() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish("10.00")));
        when(recipeItemRepository.findAllByDishId(1L)).thenReturn(List.of());

        var result = service.calculateCost(1L);

        assertThat(result.dishCost()).isEqualByComparingTo("0.00");
        assertThat(result.grossProfit()).isEqualByComparingTo("10.00");
        assertThat(result.profitMarginPercentage()).isEqualByComparingTo("100.0000");
    }

    @Test
    void rejectsMissingDish() {
        when(dishRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.calculateCost(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void rejectsMissingIngredient() {
        Dish dish = dish("10.00");
        RecipeItem item = recipeItem(dish, null, "1");
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(recipeItemRepository.findAllByDishId(1L)).thenReturn(List.of(item));

        assertThatThrownBy(() -> service.calculateCost(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void rejectsInvalidRecipeQuantity() {
        Dish dish = dish("10.00");
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(recipeItemRepository.findAllByDishId(1L)).thenReturn(List.of(recipeItem(dish, ingredient("1.00"), "0")));

        assertThatThrownBy(() -> service.calculateCost(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Recipe item quantity must be greater than zero");
    }

    @Test
    void rejectsZeroSellingPrice() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish("0.00")));
        when(recipeItemRepository.findAllByDishId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> service.calculateCost(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dish selling price must be greater than zero");
    }

    private Dish dish(String sellingPrice) {
        Dish dish = new Dish(); dish.setId(1L); dish.setName("Test dish"); dish.setSellingPrice(new BigDecimal(sellingPrice)); return dish;
    }

    private Ingredient ingredient(String cost) {
        Ingredient ingredient = new Ingredient(); ingredient.setCostPerUnit(new BigDecimal(cost)); return ingredient;
    }

    private RecipeItem recipeItem(Dish dish, Ingredient ingredient, String quantity) {
        RecipeItem item = new RecipeItem(); item.setDish(dish); item.setIngredient(ingredient); item.setQuantityRequired(new BigDecimal(quantity)); return item;
    }
}
