package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Restaurant restaurant;
    @Column(nullable = false) private String name;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Unit unit;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal costPerUnit;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal currentStock;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal minimumStock;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
}
