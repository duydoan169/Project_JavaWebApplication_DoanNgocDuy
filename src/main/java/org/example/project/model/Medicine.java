package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "price_per_unit", nullable = false)
    private BigDecimal pricePerUnit;
}