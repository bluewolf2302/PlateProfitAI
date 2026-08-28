package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> { }
