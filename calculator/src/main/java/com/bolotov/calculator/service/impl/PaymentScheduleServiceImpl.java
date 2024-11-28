package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.PaymentScheduleElementDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.service.interfaces.PaymentScheduleService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    @Override
    public List<PaymentScheduleElementDto> makePaymentSchedule(ScoringDataDto scoringDataDto, BigDecimal rate,
                                                               BigDecimal monthlyPayment) {
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        BigDecimal remainingDebt = scoringDataDto.getAmount();
        BigDecimal interestPayment;
        BigDecimal debtPayment;

        for (int i = 0; i < scoringDataDto.getTerm() - 1; i++) {
            interestPayment = remainingDebt.multiply(rate)
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
            debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            paymentSchedule.add(PaymentScheduleElementDto.builder()
                    .number(i + 1)
                    .date(LocalDate.now().plusMonths(1))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build());
        }

        interestPayment = remainingDebt.multiply(rate)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
        BigDecimal totalPayment = remainingDebt.add(interestPayment);
        debtPayment = remainingDebt;
        PaymentScheduleElementDto lastPayment = PaymentScheduleElementDto.builder()
                .number(scoringDataDto.getTerm())
                .date(LocalDate.now().plusMonths(scoringDataDto.getTerm()))
                .totalPayment(totalPayment)
                .interestPayment(interestPayment)
                .debtPayment(debtPayment)
                .remainingDebt(BigDecimal.ZERO)
                .build();

        paymentSchedule.add(lastPayment);

        return paymentSchedule;
    }
}
