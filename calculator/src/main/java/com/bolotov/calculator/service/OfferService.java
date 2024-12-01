package com.bolotov.calculator.service;

import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;

import java.util.List;

public interface OfferService {

    List<LoanOfferDto> generateOffers(LoanStatementRequestDto requestDto);

    LoanOfferDto createOffer(Boolean isInsuranceEnabled, Boolean isSalaryClient, LoanStatementRequestDto requestDto);
}
