package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.DishCostDto;
import com.plateprofit.ai.entity.Dish;
import com.plateprofit.ai.entity.Ingredient;
import com.plateprofit.ai.entity.RecipeItem;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.DishRepository;
import com.plateprofit.ai.repository.RecipeItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional(readOnly = true)
public class DishCostingService {
    private static final int MONEY_SCALE = 2;
    private static final int PERCENTAGE_SCALE = 4;

    private final DishRepository dishRepository;
    private final RecipeItemRepository recipeItemRepository;

    public DishCostingService(DishRepository dishRepository, RecipeItemRepository recipeItemRepository) {
        this.dishRepository = dishRepository;
        this.recipeItemRepository = recipeItemRepository;
    }

    public DishCostDto calculateCost(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish with id " + dishId + " was not found"));

        BigDecimal dishCost = recipeItemRepository.findAllByDishId(dishId).stream()
                .map(this::calculateRecipeItemCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        BigDecimal sellingPrice = requireSellingPrice(dish);
        BigDecimal grossProfit = sellingPrice.subtract(dishCost).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal margin = grossProfit
            .multiply(BigDecimal.valueOf(100))
            .divide(sellingPrice, PERCENTAGE_SCALE, RoundingMode.HALF_UP)
                .setScale(PERCENTAGE_SCALE, RoundingMode.HALF_UP);

        return new DishCostDto(dish.getId(), dish.getName(), sellingPrice, dishCost, grossProfit, margin);
    }

    private BigDecimal calculateRecipeItemCost(RecipeItem recipeItem) {
        BigDecimal quantity = recipeItem.getQuantityRequired();
        if (quantity == null || quantity.signum() <= 0) {
            throw new IllegalArgumentException("Recipe item quantity must be greater than zero");
        }

        Ingredient ingredient = recipeItem.getIngredient();
        if (ingredient == null) {
            throw new ResourceNotFoundException("Recipe item " + recipeItem.getId() + " has no ingredient");
        }
        BigDecimal costPerUnit = ingredient.getCostPerUnit();
        if (costPerUnit == null || costPerUnit.signum() < 0) {
            throw new IllegalArgumentException("Ingredient cost per unit must not be negative");
        }
        return quantity.multiply(costPerUnit);
    }

    private BigDecimal requireSellingPrice(Dish dish) {
        BigDecimal sellingPrice = dish.getSellingPrice();
        if (sellingPrice == null || sellingPrice.signum() <= 0) {
            throw new IllegalArgumentException("Dish selling price must be greater than zero");
        }
        return sellingPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
