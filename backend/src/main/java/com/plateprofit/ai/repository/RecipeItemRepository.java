package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {
	List<RecipeItem> findAllByDishId(Long dishId);
}
