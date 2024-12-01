package com.bolotov.calculator.service;

import java.math.BigDecimal;

public interface PrescoringService {

    BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term);

    BigDecimal calculateTotalAmount(BigDecimal amount, BigDecimal rate, Integer term, Boolean isInsuranceEnabled);
}
