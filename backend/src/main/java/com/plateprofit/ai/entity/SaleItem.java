package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal;

@Entity @Getter @Setter @NoArgsConstructor
public class SaleItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Sale sale;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Dish dish;
    @Column(nullable = false) private Integer quantity;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal sellingPrice;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal dishCostAtSale;
}
