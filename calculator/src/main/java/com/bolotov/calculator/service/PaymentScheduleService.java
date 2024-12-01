package com.bolotov.calculator.service;

import com.bolotov.calculator.dto.PaymentScheduleElementDto;
import com.bolotov.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentScheduleService {

    List<PaymentScheduleElementDto> makePaymentSchedule(ScoringDataDto scoringDataDto, BigDecimal rate, BigDecimal monthlyPayment);
}
