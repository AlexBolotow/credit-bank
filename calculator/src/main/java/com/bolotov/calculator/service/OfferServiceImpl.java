package com.bolotov.calculator.service;

import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final PrescoringService prescoringService;

    @Override
    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto requestDto) {
        return List.of(
                createOffer(false, false, requestDto),
                createOffer(true, false, requestDto),
                createOffer(false, true, requestDto),
                createOffer(true, true, requestDto)
        );
    }

    @Override
    public LoanOfferDto createOffer(Boolean isInsuranceEnabled, Boolean isSalaryClient,
                                    LoanStatementRequestDto requestDto) {
        BigDecimal rate = prescoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal monthlyPayment = prescoringService.calculateMonthlyPayment(requestDto.getAmount(), rate,
                requestDto.getTerm());
        BigDecimal totalAmount = prescoringService.calculateTotalAmount(requestDto.getAmount(), rate,
                requestDto.getTerm(), isInsuranceEnabled);

        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(requestDto.getAmount())
                .totalAmount(totalAmount)
                .term(requestDto.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
}
