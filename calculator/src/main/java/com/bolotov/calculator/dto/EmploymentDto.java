package com.bolotov.calculator.dto;

import com.bolotov.calculator.enums.EmploymentPosition;
import com.bolotov.calculator.enums.EmploymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmploymentDto {

    @NotNull
    EmploymentStatus employmentStatus;

    String employerINN;

    @NotNull
    BigDecimal salary;

    EmploymentPosition position;

    Integer workExperienceTotal;

    Integer workExperienceCurrent;
}
