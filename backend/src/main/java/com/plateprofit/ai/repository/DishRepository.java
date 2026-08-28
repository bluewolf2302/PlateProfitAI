package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DishRepository extends JpaRepository<Dish, Long> { }
