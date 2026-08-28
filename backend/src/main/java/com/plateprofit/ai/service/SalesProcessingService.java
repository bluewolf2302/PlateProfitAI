package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.SaleCreateDto;
import com.plateprofit.ai.dto.SaleDto;
import com.plateprofit.ai.dto.SaleItemRequestDto;
import com.plateprofit.ai.entity.*;
import com.plateprofit.ai.exception.InsufficientStockException;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesProcessingService {
    private static final int MONEY_SCALE = 2;
    private static final int QUANTITY_SCALE = 4;

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final RecipeItemRepository recipeItemRepository;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;

    public SalesProcessingService(RestaurantRepository restaurantRepository, DishRepository dishRepository,
                                  RecipeItemRepository recipeItemRepository, SaleRepository saleRepository,
                                  SaleItemRepository saleItemRepository, InventoryRepository inventoryRepository,
                                  InventoryTransactionRepository transactionRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.recipeItemRepository = recipeItemRepository;
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public SaleDto processSale(SaleCreateDto request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> missing("Restaurant", request.restaurantId()));
        Map<Long, IngredientRequirement> requirements = new HashMap<>();
        List<PreparedSaleItem> preparedItems = request.items().stream()
                .map(item -> prepareItem(item, restaurant, requirements))
                .toList();

        validateStock(requirements);

        Sale sale = new Sale();
        sale.setRestaurant(restaurant);
        sale.setSaleDate(request.saleDate());
        sale.setTotalAmount(preparedItems.stream()
                .map(PreparedSaleItem::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP));
        Sale savedSale = saleRepository.save(sale);

        for (PreparedSaleItem prepared : preparedItems) {
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(savedSale);
            saleItem.setDish(prepared.dish());
            saleItem.setQuantity(prepared.quantity());
            saleItem.setSellingPrice(prepared.dish().getSellingPrice().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            saleItem.setDishCostAtSale(prepared.dishCost().setScale(MONEY_SCALE, RoundingMode.HALF_UP));
            saleItemRepository.save(saleItem);
        }

        for (IngredientRequirement requirement : requirements.values()) {
            Inventory inventory = requirement.inventory();
            inventory.setCurrentQuantity(inventory.getCurrentQuantity().subtract(requirement.quantity()).setScale(QUANTITY_SCALE, RoundingMode.HALF_UP));
            inventory.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(inventory);

            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setIngredient(requirement.ingredient());
            transaction.setTransactionType(TransactionType.SALE_USAGE);
            transaction.setQuantity(requirement.quantity().setScale(QUANTITY_SCALE, RoundingMode.HALF_UP));
            transaction.setReason("Sale " + savedSale.getId());
            transactionRepository.save(transaction);
        }

        return new SaleDto(savedSale.getId(), restaurant.getId(), savedSale.getSaleDate(), savedSale.getTotalAmount());
    }

    private PreparedSaleItem prepareItem(SaleItemRequestDto request, Restaurant restaurant,
                                         Map<Long, IngredientRequirement> requirements) {
        Dish dish = dishRepository.findById(request.dishId())
                .orElseThrow(() -> missing("Dish", request.dishId()));
        if (!dish.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Dish " + dish.getId() + " does not belong to restaurant " + restaurant.getId());
        }
        if (dish.getSellingPrice() == null || dish.getSellingPrice().signum() <= 0) {
            throw new IllegalArgumentException("Dish selling price must be greater than zero");
        }

        BigDecimal dishCost = BigDecimal.ZERO;
        for (RecipeItem recipeItem : recipeItemRepository.findAllByDishId(dish.getId())) {
            BigDecimal quantityRequired = recipeItem.getQuantityRequired();
            Ingredient ingredient = recipeItem.getIngredient();
            if (quantityRequired == null || quantityRequired.signum() <= 0) {
                throw new IllegalArgumentException("Recipe item quantity must be greater than zero");
            }
            if (ingredient == null) {
                throw new ResourceNotFoundException("Recipe item " + recipeItem.getId() + " has no ingredient");
            }
            if (ingredient.getCostPerUnit() == null || ingredient.getCostPerUnit().signum() < 0) {
                throw new IllegalArgumentException("Ingredient cost per unit must not be negative");
            }
            BigDecimal perDishCost = quantityRequired.multiply(ingredient.getCostPerUnit());
            dishCost = dishCost.add(perDishCost);
            IngredientRequirement requirement = requirements.computeIfAbsent(ingredient.getId(), id -> createRequirement(ingredient, restaurant));
            requirement.add(quantityRequired.multiply(BigDecimal.valueOf(request.quantity())));
        }
        return new PreparedSaleItem(dish, request.quantity(), dishCost, dish.getSellingPrice().multiply(BigDecimal.valueOf(request.quantity())));
    }

    private IngredientRequirement createRequirement(Ingredient ingredient, Restaurant restaurant) {
        Inventory inventory = inventoryRepository.findByIngredientId(ingredient.getId())
                .orElseThrow(() -> missing("Inventory for ingredient", ingredient.getId()));
        if (!inventory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Inventory does not belong to the sale restaurant");
        }
        if (inventory.getCurrentQuantity() == null || inventory.getCurrentQuantity().signum() < 0) {
            throw new IllegalArgumentException("Inventory quantity must not be negative");
        }
        return new IngredientRequirement(ingredient, inventory);
    }

    private void validateStock(Map<Long, IngredientRequirement> requirements) {
        requirements.values().forEach(requirement -> {
            if (requirement.quantity().compareTo(requirement.inventory().getCurrentQuantity()) > 0) {
                throw new InsufficientStockException("Insufficient stock for ingredient " + requirement.ingredient().getName());
            }
        });
    }

    private ResourceNotFoundException missing(String type, Long id) {
        return new ResourceNotFoundException(type + " with id " + id + " was not found");
    }

    private record PreparedSaleItem(Dish dish, Integer quantity, BigDecimal dishCost, BigDecimal revenue) { }

    private static final class IngredientRequirement {
        private final Ingredient ingredient;
        private final Inventory inventory;
        private BigDecimal quantity = BigDecimal.ZERO;

        private IngredientRequirement(Ingredient ingredient, Inventory inventory) {
            this.ingredient = ingredient;
            this.inventory = inventory;
        }

        private void add(BigDecimal additionalQuantity) { quantity = quantity.add(additionalQuantity); }
        private Ingredient ingredient() { return ingredient; }
        private Inventory inventory() { return inventory; }
        private BigDecimal quantity() { return quantity; }
    }
}
