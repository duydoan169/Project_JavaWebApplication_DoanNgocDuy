package org.example.project.dto;

import lombok.Data;

@Data
public class PrescriptionDetailDTO {
    private Long medicineId;
    private Integer quantity;
    private String dosage;
}