package org.example.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MedicineDTO {

    private Long id;

    @NotBlank(message = "Tên thuốc không được để trống")
    private String name;

    @NotBlank(message = "Đơn vị không được để trống")
    private String unit;

    @Min(value = 0, message = "Số lượng không được âm")
    private int stockQuantity;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá không được âm")
    private BigDecimal pricePerUnit;
}