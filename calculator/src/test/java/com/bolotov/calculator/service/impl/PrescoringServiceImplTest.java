package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.service.interfaces.PrescoringService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrescoringServiceImplTest {

    @Autowired
    PrescoringService prescoringService;

    @Test
    @DisplayName("Test calculate monthly payment functionality")
    public void calculateMonthlyPayment() {
        //when
        BigDecimal mp1 = prescoringService.calculateMonthlyPayment(BigDecimal.valueOf(20000), BigDecimal.valueOf(0.2), 6);
        BigDecimal mp2 = prescoringService.calculateMonthlyPayment(BigDecimal.valueOf(55555), BigDecimal.valueOf(0.17), 8);

        //then
        assertThat(mp1).isEqualByComparingTo(BigDecimal.valueOf(3530.46));
        assertThat(mp2).isEqualByComparingTo(BigDecimal.valueOf(7394.34));
    }

    @Test
    @DisplayName("Test calculate total amount functionality")
    public void calculateTotalAmount() {
        //when
        BigDecimal ta1 = prescoringService.calculateTotalAmount(BigDecimal.valueOf(4_000_000), BigDecimal.valueOf(0.17),
                18, true);
        BigDecimal ta2 = prescoringService.calculateTotalAmount(BigDecimal.valueOf(730_000), BigDecimal.valueOf(0.21),
                9, false);

        //then
        assertThat(ta1).isEqualByComparingTo(BigDecimal.valueOf(4679766.84));
        assertThat(ta2).isEqualByComparingTo(BigDecimal.valueOf(795351.96));
    }
}