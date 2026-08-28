package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	Optional<Inventory> findByIngredientId(Long ingredientId);
}
