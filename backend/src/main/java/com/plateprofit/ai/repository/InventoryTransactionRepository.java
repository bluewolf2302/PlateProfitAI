package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> { }
