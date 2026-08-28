package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal;

@Entity @Getter @Setter @NoArgsConstructor
public class RecipeItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Dish dish;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Ingredient ingredient;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal quantityRequired;
}
