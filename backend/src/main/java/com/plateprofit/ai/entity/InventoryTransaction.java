package com.plateprofit.ai.entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.math.BigDecimal; import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor
public class InventoryTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) private Ingredient ingredient;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionType transactionType;
    @Column(nullable = false, precision = 12, scale = 4) private BigDecimal quantity;
    private String reason;
    @Column(nullable = false) private LocalDateTime transactionDate = LocalDateTime.now();
}
