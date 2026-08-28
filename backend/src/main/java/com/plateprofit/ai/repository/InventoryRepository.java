package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryRepository extends JpaRepository<Inventory, Long> { }
