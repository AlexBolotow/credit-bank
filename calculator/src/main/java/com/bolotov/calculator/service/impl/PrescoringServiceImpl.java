package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.service.interfaces.PrescoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PrescoringServiceImpl implements PrescoringService {

    @Value("${calculator.base.rate}")
    BigDecimal baseRate;

    @Value("${calculator.insurance.discount}")
    BigDecimal insuranceDiscount;

    @Value("${calculator.insurance.rate}")
    BigDecimal insuranceRate;

    @Value("${calculator.non.salary.surcharge}")
    BigDecimal nonSalarySurcharge;


    @Override
    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        BigDecimal rate = baseRate;
        if (isInsuranceEnabled) {
            rate = rate.subtract(insuranceDiscount);
        }

        if (!isSalaryClient) {
            rate = rate.add(nonSalarySurcharge);
        }

        return rate;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
        BigDecimal annuityCoefficient = monthlyRate.multiply(monthlyRate.add(BigDecimal.ONE).pow(term))
                .divide(monthlyRate.add(BigDecimal.ONE).pow(term).subtract(BigDecimal.ONE), 10, RoundingMode.HALF_EVEN);
        return amount.multiply(annuityCoefficient).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount, BigDecimal rate, Integer term, Boolean isInsuranceEnabled) {
        BigDecimal totalAmount = calculateMonthlyPayment(amount, rate, term).multiply(BigDecimal.valueOf(term));
        if (isInsuranceEnabled) {
            totalAmount = totalAmount.add(amount.multiply(insuranceRate));
        }

        return totalAmount;
    }
}
