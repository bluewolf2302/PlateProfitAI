package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> { }
