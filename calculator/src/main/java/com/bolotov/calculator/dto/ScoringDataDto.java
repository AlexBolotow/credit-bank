package com.bolotov.calculator.dto;

import com.bolotov.calculator.enums.Gender;
import com.bolotov.calculator.enums.MaritalStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoringDataDto {
    BigDecimal amount;
    Integer term;
    String firstName;
    String lastName;
    String middleName;
    Gender gender;
    LocalDate birthday;
    String passportSeries;
    String passportNumber;
    LocalDate passportIssueDate;
    String passportIssueBranch;
    MaritalStatus maritalStatus;
    Integer dependentAmount;
    EmploymentDto employment;
    String accountNumber;
    Boolean isInsuranceEnabled;
    Boolean isSalaryClient;
}
