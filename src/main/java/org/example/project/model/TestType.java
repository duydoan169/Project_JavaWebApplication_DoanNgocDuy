package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit")
    private String unit;
}