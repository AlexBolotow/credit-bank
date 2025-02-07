package com.bolotov.calculator.service;

import com.bolotov.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoringService {

    BigDecimal calculateRate(ScoringDataDto scoringDataDto);

    BigDecimal calculatePsk(ScoringDataDto scoringDataDto, BigDecimal rate);

    BigDecimal calculateMonthlyPayment(ScoringDataDto scoringDataDto, BigDecimal rate);
}
