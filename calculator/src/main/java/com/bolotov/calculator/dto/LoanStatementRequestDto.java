package com.bolotov.calculator.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanStatementRequestDto {
    BigDecimal amount;
    Integer term;
    String firstName;
    String lastName;
    String middleName;
    String email;
    LocalDate birthdayDate;
    String passportSeries;
    String passportNumber;
}
