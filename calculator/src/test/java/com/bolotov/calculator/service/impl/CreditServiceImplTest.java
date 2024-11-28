package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.CreditDto;
import com.bolotov.calculator.dto.PaymentScheduleElementDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.utils.DataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    ScoringServiceImpl scoringService;

    @Mock
    PaymentScheduleServiceImpl paymentScheduleService;

    @InjectMocks
    CreditServiceImpl creditService;

    @Test
    void createCredit() {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        BigDecimal rate = BigDecimal.valueOf(0.02);
        BigDecimal monthlyPayment = BigDecimal.valueOf(1000);
        BigDecimal psk = BigDecimal.valueOf(0.15);
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();

        BDDMockito.when(scoringService.calculateRate(any()))
                .thenReturn(rate);
        BDDMockito.when(scoringService.calculateMonthlyPayment(any(), any()))
                .thenReturn(monthlyPayment);
        BDDMockito.when(scoringService.calculatePsk(any(), any()))
                .thenReturn(psk);
        BDDMockito.when(paymentScheduleService.makePaymentSchedule(any(), any(), any()))
                .thenReturn(paymentSchedule);

        //when
        CreditDto creditDto = creditService.createCredit(scoringDataDto);

        //then
        verify(scoringService, times(1)).calculateRate(any());
        verify(scoringService, times(1)).calculateMonthlyPayment(any(), any());
        verify(scoringService, times(1)).calculatePsk(any(), any());
        verify(paymentScheduleService, times(1)).makePaymentSchedule(any(), any(), any());

        assertThat(creditDto.getRate()).isEqualByComparingTo(rate);
        assertThat(creditDto.getMonthlyPayment()).isEqualByComparingTo(monthlyPayment);
        assertThat(creditDto.getPsk()).isEqualByComparingTo(psk);
        assertThat(creditDto.getPaymentSchedule()).isNotNull();
        assertThat(creditDto.getPaymentSchedule()).hasSize(paymentSchedule.size());
    }
}