package com.bolotov.calculator.controller;

import com.bolotov.calculator.dto.CreditDto;
import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.enums.EmploymentStatus;
import com.bolotov.calculator.exception.ScoringException;
import com.bolotov.calculator.service.interfaces.CreditService;
import com.bolotov.calculator.service.interfaces.OfferService;
import com.bolotov.calculator.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
class CalculatorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OfferService offerService;

    @MockBean
    private CreditService creditService;

    @Test
    @DisplayName("Test get offers functionality")
    void givenLoanStatementRequestDto_whenGetOffers_thenSuccessResponse() throws Exception {
        //given
        LoanStatementRequestDto request = DataUtils.getLoanStatementRequestDtoJohnDoe();
        List<LoanOfferDto> offers = DataUtils.getOffers();
        BDDMockito.when(offerService.generateOffers(any(LoanStatementRequestDto.class)))
                .thenReturn(offers);

        //when
        ResultActions result = mockMvc.perform(post("http://localhost:8080/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)));

        for (int i = 0; i < offers.size(); i++) {
            result
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].statementId", Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].term", Matchers.is(offers.get(i).getTerm())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].isInsuranceEnabled", Matchers.is(offers.get(i).getIsInsuranceEnabled())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].isSalaryClient", Matchers.is(offers.get(i).getIsSalaryClient())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].requestedAmount", Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].totalAmount", Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].rate", Matchers.notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[" + i + "].monthlyPayment", Matchers.notNullValue()));
        }
    }

    @Test
    @DisplayName("Test get offers by incorrect request functionality")
    void givenIncorrectLoanStatementRequestDto_whenGetOffers_thenErrorResponse() throws Exception {
        //given
        LoanStatementRequestDto request = DataUtils.getIncorrectLoanStatementRequestDtoJohnDoe();

        //when
        ResultActions result = mockMvc.perform(post("http://localhost:8080/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.notNullValue()));
    }

    @Test
    @DisplayName("Test get credit functionality")
    void givenScoringDataDto_whenGetCredit_thenSuccessResponse() throws Exception {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        CreditDto creditDto = DataUtils.getCreditDto();

        BDDMockito.when(creditService.createCredit(scoringDataDto))
                .thenReturn(creditDto);

        //when
        ResultActions result = mockMvc.perform(post("http://localhost:8080/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoringDataDto)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount", Matchers.is(creditDto.getAmount().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate", Matchers.is(creditDto.getRate().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyPayment", Matchers.is(creditDto.getMonthlyPayment().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.psk", Matchers.is(creditDto.getPsk().doubleValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isInsuranceEnabled", Matchers.is(creditDto.getIsInsuranceEnabled())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSalaryClient", Matchers.is(creditDto.getIsSalaryClient())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentSchedule", Matchers.hasSize(creditDto.getPaymentSchedule().size())));
    }

    @Test
    @DisplayName("Test get credit by incorrect data functionality")
    void givenIncorrectScoringDataDto_whenGetCredit_thenErrorResponse() throws Exception {
        //given
        ScoringDataDto scoringDataDto = DataUtils.getScoringDataDtoJohnDoe();
        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        BDDMockito.given(creditService.createCredit(any(ScoringDataDto.class)))
                .willThrow(new ScoringException("Unemployed client"));

        //when
        ResultActions result = mockMvc.perform(post("http://localhost:8080/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoringDataDto)));

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(422)));
    }
}