package com.bolotov.calculator.utils;

import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class DataUtils {

    public static LoanStatementRequestDto getLoanStatementRequestDtoJohnDoe() {
        return LoanStatementRequestDto.builder()
                .amount(new BigDecimal("20000"))
                .term(12)
                .firstName("John")
                .lastName("Doe")
                .middleName("AAA")
                .email("john.doe@example.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    public static LoanStatementRequestDto getIncorrectLoanStatementRequestDtoJohnDoe() {
        return LoanStatementRequestDto.builder()
                .amount(new BigDecimal("10000"))
                .term(4)
                .firstName("John")
                .lastName("Doe")
                .middleName("A")
                .email("john.doe.com")
                .birthdate(LocalDate.of(2024, 1, 1))
                .passportSeries("12")
                .passportNumber("1")
                .build();
    }

    public static List<LoanOfferDto> getOffers() {
        return List.of(
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .term(12)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .requestedAmount(new BigDecimal("15000"))
                        .totalAmount(new BigDecimal("18000"))
                        .rate(new BigDecimal("0.10"))
                        .monthlyPayment(new BigDecimal("1500"))
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .term(12)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .requestedAmount(new BigDecimal("17000"))
                        .totalAmount(new BigDecimal("20000"))
                        .rate(new BigDecimal("0.09"))
                        .monthlyPayment(new BigDecimal("1700"))
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .term(12)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .requestedAmount(new BigDecimal("19000"))
                        .totalAmount(new BigDecimal("22000"))
                        .rate(new BigDecimal("0.08"))
                        .monthlyPayment(new BigDecimal("1900"))
                        .build(),
                LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .term(12)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .requestedAmount(new BigDecimal("30000"))
                        .totalAmount(new BigDecimal("22000"))
                        .rate(new BigDecimal("0.07"))
                        .monthlyPayment(new BigDecimal("1700"))
                        .build()
        );
    }
}
