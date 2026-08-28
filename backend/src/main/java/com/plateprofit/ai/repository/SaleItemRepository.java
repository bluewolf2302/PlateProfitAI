package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
	List<SaleItem> findAllBySaleId(Long saleId);

	@org.springframework.data.jpa.repository.Query("select coalesce(sum(si.dishCostAtSale * si.quantity), 0) from SaleItem si where si.sale.restaurant.id = :restaurantId and si.sale.saleDate between :startDate and :endDate")
	BigDecimal sumDishCosts(Long restaurantId, LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("select coalesce(sum(si.sellingPrice * si.quantity), 0) from SaleItem si where si.dish.id = :dishId and si.sale.saleDate between :startDate and :endDate")
	BigDecimal sumDishRevenue(Long dishId, LocalDate startDate, LocalDate endDate);

	@org.springframework.data.jpa.repository.Query("select coalesce(sum(si.dishCostAtSale * si.quantity), 0) from SaleItem si where si.dish.id = :dishId and si.sale.saleDate between :startDate and :endDate")
	BigDecimal sumDishCost(Long dishId, LocalDate startDate, LocalDate endDate);
}
