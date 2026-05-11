package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescription_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "dosage", nullable = false)
    private String dosage;
}