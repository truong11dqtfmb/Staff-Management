package com.dq.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class StaffDTO {

    @NotNull(message = "Name can not be null!!")
    @NotEmpty(message = "Name can not be empty!!")
    private String name;

    private boolean gender;

    @NotNull
    @Size(max = 10, min = 10, message = "Mobile number should be of 10 digits")
    @Pattern(regexp = "[0-9]{10}", message = "Mobile number is invalid!!")
    private String phone;

    @NotEmpty(message = "Email can not be empty!!")
    private String email;

    private MultipartFile photo;

    @DecimalMin(value = "0.1", inclusive = false, message = "Not Less than 0.1")
    @DecimalMax(value = "100000000", inclusive = false, message = "Not greater than 100000000")
    private float salary;

}
