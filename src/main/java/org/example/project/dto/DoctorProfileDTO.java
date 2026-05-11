package org.example.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import org.example.project.model.enums.Gender;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DoctorProfileDTO {

    @NotBlank(message = "Tên không được để trống")
    private String fullName;
    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại không hợp lệ")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Ngày sinh không được ở trong tương lai")
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;

    @NotNull(message = "Chuyên môn không được để trống")
    private Long specialtyId;
}