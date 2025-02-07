package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private PrescoringServiceImpl prescoringService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    @DisplayName("Test generate offers functionality")
    void generateOffers() {
        //given
        BigDecimal amount = BigDecimal.valueOf(100_000);
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        requestDto.setAmount(amount);

        BigDecimal totalAmountInsTrue = BigDecimal.valueOf(100_002);
        BigDecimal totalAmountInsFalse = BigDecimal.valueOf(100_005);

        BDDMockito.when(prescoringService.calculateTotalAmount(any(), any(), any(), any()))
                .thenReturn(totalAmountInsTrue, totalAmountInsFalse, totalAmountInsTrue, totalAmountInsFalse);

        //when
        List<LoanOfferDto> loanOfferDtos = offerService.generateOffers(requestDto);

        //then
        assertThat(loanOfferDtos.size()).isEqualTo(4);
        assertThat(loanOfferDtos.get(0).getRequestedAmount()).isEqualTo(amount);
        verify(prescoringService, times(4)).calculateTotalAmount(any(), any(), any(), any());
        verify(prescoringService, times(4)).calculateRate(any(), any());
        verify(prescoringService, times(4)).calculateMonthlyPayment(any(), any(), any());

        assertThat(loanOfferDtos.get(0).getTotalAmount()).isEqualTo(totalAmountInsTrue);
        assertThat(loanOfferDtos.get(1).getTotalAmount()).isEqualTo(totalAmountInsFalse);
        assertThat(loanOfferDtos.get(2).getTotalAmount()).isEqualTo(totalAmountInsTrue);
        assertThat(loanOfferDtos.get(3).getTotalAmount()).isEqualTo(totalAmountInsFalse);
    }

    @Test
    void createOffer() {
    }
}