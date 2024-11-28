package com.bolotov.calculator.service;

import com.bolotov.calculator.dto.EmploymentDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.exception.ScoringException;
import com.bolotov.calculator.utils.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

@ExtendWith(MockitoExtension.class)
class ScoringServiceImplTest {

    @Mock
    private PrescoringServiceImpl prescoringService;

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Test
    @DisplayName("Test calculate rate functionality")
    void givenScoringDataDto_whenCalculateRate_thenReturn() {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        BDDMockito.when(prescoringService.calculateRate(anyBoolean(), anyBoolean()))
                .thenReturn(BigDecimal.valueOf(0.17));

        //when
        BigDecimal rate = scoringService.calculateRate(scoringDataDto);

        //then
        assertThat(rate).isNotNull();
        assertThat(rate).isEqualByComparingTo(BigDecimal.valueOf(0.09));
    }

    @Test
    @DisplayName("Test calculate rate with low salary functionality")
    void givenScoringDataDtoWithLowSalary_whenCalculateRate_thenExceptionIsThrown() {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        scoringDataDto.getEmployment().setSalary(BigDecimal.valueOf(500));

        BDDMockito.when(prescoringService.calculateRate(anyBoolean(), anyBoolean()))
                .thenReturn(BigDecimal.valueOf(0.17));

        //when
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDto),
                "Not enough client salary");
    }

    @Test
    @DisplayName("Test calculate psk functionality")
    void calculatePsk() {
        //given
        ScoringDataDto scoringDataDto1 = DataUtils.getScoringDataDtoJohnDoe();
        scoringDataDto1.setIsInsuranceEnabled(false);

        BigDecimal rate = BigDecimal.valueOf(0.2);

        BDDMockito.when(prescoringService.calculateMonthlyPayment(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(1852.69));

        //when
        BigDecimal psk1 = scoringService.calculatePsk(scoringDataDto1, rate);

        //then
        assertThat(psk1).isEqualByComparingTo(BigDecimal.valueOf(0.112));
    }
}