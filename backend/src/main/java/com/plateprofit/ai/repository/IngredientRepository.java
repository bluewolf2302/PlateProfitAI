package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IngredientRepository extends JpaRepository<Ingredient, Long> { }
