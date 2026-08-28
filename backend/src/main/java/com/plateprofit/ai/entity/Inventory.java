package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
public class Inventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Restaurant restaurant;
    @OneToOne(fetch = FetchType.LAZY, optional = false) private Ingredient ingredient;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal currentQuantity;
    @Column(nullable = false) private LocalDateTime lastUpdated = LocalDateTime.now();
}
