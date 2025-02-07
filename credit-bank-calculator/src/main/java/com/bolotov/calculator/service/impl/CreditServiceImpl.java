package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.CreditDto;
import com.bolotov.calculator.dto.PaymentScheduleElementDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.service.CreditService;
import com.bolotov.calculator.service.PaymentScheduleService;
import com.bolotov.calculator.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final ScoringService scoringService;

    private final PaymentScheduleService paymentScheduleService;

    @Override
    public CreditDto createCredit(ScoringDataDto scoringDataDto) {
        BigDecimal rate = scoringService.calculateRate(scoringDataDto);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(scoringDataDto, rate);
        BigDecimal psk = scoringService.calculatePsk(scoringDataDto, rate);
        List<PaymentScheduleElementDto> paymentSchedule = paymentScheduleService.makePaymentSchedule(scoringDataDto, rate,
                monthlyPayment);

        return CreditDto.builder()
                .amount(scoringDataDto.getAmount())
                .term(scoringDataDto.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
}
