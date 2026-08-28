package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.*;
import com.plateprofit.ai.entity.*;
import com.plateprofit.ai.exception.ResourceNotFoundException;
import com.plateprofit.ai.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ResourceCrudService {
    private final RestaurantRepository restaurants;
    private final DishRepository dishes;
    private final IngredientRepository ingredients;
    private final RecipeItemRepository recipes;
    private final SaleRepository sales;
    private final InventoryRepository inventory;
    private final ExpenseRepository expenses;

    public ResourceCrudService(RestaurantRepository restaurants, DishRepository dishes, IngredientRepository ingredients,
                               RecipeItemRepository recipes, SaleRepository sales, InventoryRepository inventory,
                               ExpenseRepository expenses) {
        this.restaurants = restaurants; this.dishes = dishes; this.ingredients = ingredients;
        this.recipes = recipes; this.sales = sales; this.inventory = inventory; this.expenses = expenses;
    }

    public List<RestaurantDto> restaurants() { return restaurants.findAll().stream().map(this::toDto).toList(); }
    public RestaurantDto restaurant(Long id) { return toDto(restaurants.findById(id).orElseThrow(() -> missing("Restaurant", id))); }
    public RestaurantDto create(RestaurantDto dto) { return toDto(restaurants.save(from(dto))); }
    public RestaurantDto update(Long id, RestaurantDto dto) { Restaurant entity = restaurants.findById(id).orElseThrow(() -> missing("Restaurant", id)); entity.setName(dto.name()); entity.setAddress(dto.address()); return toDto(entity); }
    public void deleteRestaurant(Long id) { restaurants.delete(findRestaurant(id)); }

    public List<DishDto> dishes() { return dishes.findAll().stream().map(this::toDto).toList(); }
    public DishDto dish(Long id) { return toDto(dishes.findById(id).orElseThrow(() -> missing("Dish", id))); }
    public DishDto create(DishDto dto) { Dish e = new Dish(); apply(e, dto); return toDto(dishes.save(e)); }
    public DishDto update(Long id, DishDto dto) { Dish e = dishes.findById(id).orElseThrow(() -> missing("Dish", id)); apply(e, dto); return toDto(e); }
    public void deleteDish(Long id) { dishes.delete(findDish(id)); }

    public List<IngredientDto> ingredients() { return ingredients.findAll().stream().map(this::toDto).toList(); }
    public IngredientDto ingredient(Long id) { return toDto(ingredients.findById(id).orElseThrow(() -> missing("Ingredient", id))); }
    public IngredientDto create(IngredientDto dto) { Ingredient e = new Ingredient(); apply(e, dto); return toDto(ingredients.save(e)); }
    public IngredientDto update(Long id, IngredientDto dto) { Ingredient e = ingredients.findById(id).orElseThrow(() -> missing("Ingredient", id)); apply(e, dto); return toDto(e); }
    public void deleteIngredient(Long id) { ingredients.delete(findIngredient(id)); }

    public List<RecipeItemDto> recipes() { return recipes.findAll().stream().map(this::toDto).toList(); }
    public RecipeItemDto recipe(Long id) { return toDto(recipes.findById(id).orElseThrow(() -> missing("Recipe item", id))); }
    public RecipeItemDto create(RecipeItemDto dto) { RecipeItem e = new RecipeItem(); apply(e, dto); return toDto(recipes.save(e)); }
    public RecipeItemDto update(Long id, RecipeItemDto dto) { RecipeItem e = recipes.findById(id).orElseThrow(() -> missing("Recipe item", id)); apply(e, dto); return toDto(e); }
    public void deleteRecipe(Long id) { recipes.delete(recipes.findById(id).orElseThrow(() -> missing("Recipe item", id))); }

    public List<SaleDto> sales() { return sales.findAll().stream().map(this::toDto).toList(); }
    public SaleDto sale(Long id) { return toDto(sales.findById(id).orElseThrow(() -> missing("Sale", id))); }
    public SaleDto create(SaleDto dto) { Sale e = new Sale(); apply(e, dto); return toDto(sales.save(e)); }
    public SaleDto update(Long id, SaleDto dto) { Sale e = sales.findById(id).orElseThrow(() -> missing("Sale", id)); apply(e, dto); return toDto(e); }
    public void deleteSale(Long id) { sales.delete(sales.findById(id).orElseThrow(() -> missing("Sale", id))); }

    public List<InventoryDto> inventory() { return inventory.findAll().stream().map(this::toDto).toList(); }
    public InventoryDto inventoryItem(Long id) { return toDto(inventory.findById(id).orElseThrow(() -> missing("Inventory", id))); }
    public InventoryDto create(InventoryDto dto) { Inventory e = new Inventory(); apply(e, dto); return toDto(inventory.save(e)); }
    public InventoryDto update(Long id, InventoryDto dto) { Inventory e = inventory.findById(id).orElseThrow(() -> missing("Inventory", id)); apply(e, dto); return toDto(e); }
    public void deleteInventory(Long id) { inventory.delete(inventory.findById(id).orElseThrow(() -> missing("Inventory", id))); }

    public List<ExpenseDto> expenses() { return expenses.findAll().stream().map(this::toDto).toList(); }
    public ExpenseDto expense(Long id) { return toDto(expenses.findById(id).orElseThrow(() -> missing("Expense", id))); }
    public ExpenseDto create(ExpenseDto dto) { Expense e = new Expense(); apply(e, dto); return toDto(expenses.save(e)); }
    public ExpenseDto update(Long id, ExpenseDto dto) { Expense e = expenses.findById(id).orElseThrow(() -> missing("Expense", id)); apply(e, dto); return toDto(e); }
    public void deleteExpense(Long id) { expenses.delete(expenses.findById(id).orElseThrow(() -> missing("Expense", id))); }

    private Restaurant findRestaurant(Long id) { return restaurants.findById(id).orElseThrow(() -> missing("Restaurant", id)); }
    private Dish findDish(Long id) { return dishes.findById(id).orElseThrow(() -> missing("Dish", id)); }
    private Ingredient findIngredient(Long id) { return ingredients.findById(id).orElseThrow(() -> missing("Ingredient", id)); }
    private ResourceNotFoundException missing(String type, Long id) { return new ResourceNotFoundException(type + " with id " + id + " was not found"); }
    private Restaurant from(RestaurantDto d) { Restaurant e = new Restaurant(); e.setName(d.name()); e.setAddress(d.address()); return e; }
    private void apply(Dish e, DishDto d) { e.setRestaurant(findRestaurant(d.restaurantId())); e.setName(d.name()); e.setDescription(d.description()); e.setCategory(d.category()); e.setSellingPrice(d.sellingPrice()); e.setActive(d.active()); }
    private void apply(Ingredient e, IngredientDto d) { e.setRestaurant(findRestaurant(d.restaurantId())); e.setName(d.name()); e.setUnit(d.unit()); e.setCostPerUnit(d.costPerUnit()); e.setCurrentStock(d.currentStock()); e.setMinimumStock(d.minimumStock()); }
    private void apply(RecipeItem e, RecipeItemDto d) { e.setDish(findDish(d.dishId())); e.setIngredient(findIngredient(d.ingredientId())); e.setQuantityRequired(d.quantityRequired()); }
    private void apply(Sale e, SaleDto d) { e.setRestaurant(findRestaurant(d.restaurantId())); e.setSaleDate(d.saleDate()); e.setTotalAmount(d.totalAmount()); }
    private void apply(Inventory e, InventoryDto d) { e.setRestaurant(findRestaurant(d.restaurantId())); e.setIngredient(findIngredient(d.ingredientId())); e.setCurrentQuantity(d.currentQuantity()); e.setLastUpdated(java.time.LocalDateTime.now()); }
    private void apply(Expense e, ExpenseDto d) { e.setRestaurant(findRestaurant(d.restaurantId())); e.setCategory(d.category()); e.setDescription(d.description()); e.setAmount(d.amount()); e.setExpenseDate(d.expenseDate()); e.setBillFileUrl(d.billFileUrl()); }
    private RestaurantDto toDto(Restaurant e) { return new RestaurantDto(e.getId(), e.getName(), e.getAddress()); }
    private DishDto toDto(Dish e) { return new DishDto(e.getId(), e.getRestaurant().getId(), e.getName(), e.getDescription(), e.getCategory(), e.getSellingPrice(), e.isActive()); }
    private IngredientDto toDto(Ingredient e) { return new IngredientDto(e.getId(), e.getRestaurant().getId(), e.getName(), e.getUnit(), e.getCostPerUnit(), e.getCurrentStock(), e.getMinimumStock()); }
    private RecipeItemDto toDto(RecipeItem e) { return new RecipeItemDto(e.getId(), e.getDish().getId(), e.getIngredient().getId(), e.getQuantityRequired()); }
    private SaleDto toDto(Sale e) { return new SaleDto(e.getId(), e.getRestaurant().getId(), e.getSaleDate(), e.getTotalAmount()); }
    private InventoryDto toDto(Inventory e) { return new InventoryDto(e.getId(), e.getRestaurant().getId(), e.getIngredient().getId(), e.getCurrentQuantity()); }
    private ExpenseDto toDto(Expense e) { return new ExpenseDto(e.getId(), e.getRestaurant().getId(), e.getCategory(), e.getDescription(), e.getAmount(), e.getExpenseDate(), e.getBillFileUrl()); }
}
