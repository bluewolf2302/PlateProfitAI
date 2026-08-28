package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
public class Dish {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Restaurant restaurant;
    @Column(nullable = false) private String name;
    private String description; private String category;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal sellingPrice;
    @Column(nullable = false) private boolean active = true;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
}
