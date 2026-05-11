package org.example.project.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {

    @NotNull(message = "Vui lòng chọn bác sĩ")
    private Long doctorId;

    @NotNull(message = "Vui lòng chọn ngày khám")
    @FutureOrPresent(message = "Ngày khám phải là ngày trong tương lai")
    private LocalDate appointmentDate;

    @NotNull(message = "Vui lòng chọn giờ khám")
    private LocalTime startTime;

    private String reason;
}