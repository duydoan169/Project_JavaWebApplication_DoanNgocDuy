package org.example.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExaminationDTO {

    @NotNull(message = "Không tìm thấy lịch hẹn")
    private Long appointmentId;

    @NotBlank(message = "Triệu chứng không được để trống")
    private String symptoms;

    @NotBlank(message = "Chẩn đoán không được để trống")
    private String diagnosis;

    private String notes;

    private String instructions;

    private List<PrescriptionDetailDTO> medicines = new ArrayList<>(List.of(
            new PrescriptionDetailDTO(),
            new PrescriptionDetailDTO(),
            new PrescriptionDetailDTO(),
            new PrescriptionDetailDTO(),
            new PrescriptionDetailDTO()
    ));
}