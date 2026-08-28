package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
public interface SaleRepository extends JpaRepository<Sale, Long> {
	@org.springframework.data.jpa.repository.Query("select coalesce(sum(s.totalAmount), 0) from Sale s where s.restaurant.id = :restaurantId and s.saleDate between :startDate and :endDate")
	BigDecimal sumRevenue(Long restaurantId, LocalDate startDate, LocalDate endDate);
}
