package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.PaymentScheduleElementDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.utils.DataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentScheduleServiceImplTest {

    @Autowired
    PaymentScheduleServiceImpl paymentScheduleService;

    @Test
    void makePaymentSchedule() {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        scoringDataDto.setTerm(4);
        BigDecimal rate = BigDecimal.valueOf(0.2);
        BigDecimal monthlyPayment = BigDecimal.valueOf(5210.06);

        //when
        List<PaymentScheduleElementDto> paymentSchedule = paymentScheduleService.makePaymentSchedule(scoringDataDto,
                rate, monthlyPayment);

        //then
        assertThat(paymentSchedule).isNotNull();
        assertThat(paymentSchedule).hasSize(scoringDataDto.getTerm());

        assertThat(paymentSchedule.get(0).getInterestPayment()).isEqualByComparingTo(BigDecimal.valueOf(333.33));
        assertThat(paymentSchedule.get(0).getDebtPayment()).isEqualByComparingTo(BigDecimal.valueOf(4876.73));
        assertThat(paymentSchedule.get(0).getRemainingDebt()).isEqualByComparingTo(BigDecimal.valueOf(15123.27));

        assertThat(paymentSchedule.get(1).getInterestPayment()).isEqualByComparingTo(BigDecimal.valueOf(252.05));
        assertThat(paymentSchedule.get(1).getDebtPayment()).isEqualByComparingTo(BigDecimal.valueOf(4958.01));
        assertThat(paymentSchedule.get(1).getRemainingDebt()).isEqualByComparingTo(BigDecimal.valueOf(10165.26));

        assertThat(paymentSchedule.get(2).getInterestPayment()).isEqualByComparingTo(BigDecimal.valueOf(169.42));
        assertThat(paymentSchedule.get(2).getDebtPayment()).isEqualByComparingTo(BigDecimal.valueOf(5040.64));
        assertThat(paymentSchedule.get(2).getRemainingDebt()).isEqualByComparingTo(BigDecimal.valueOf(5124.62));

        assertThat(paymentSchedule.get(3).getInterestPayment()).isEqualByComparingTo(BigDecimal.valueOf(85.41));
        assertThat(paymentSchedule.get(3).getDebtPayment()).isEqualByComparingTo(BigDecimal.valueOf(5124.62));
        assertThat(paymentSchedule.get(3).getRemainingDebt()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}