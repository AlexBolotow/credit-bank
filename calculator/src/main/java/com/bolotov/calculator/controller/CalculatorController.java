package com.bolotov.calculator.controller;

import com.bolotov.calculator.dto.CreditDto;
import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.service.CreditService;
import com.bolotov.calculator.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final OfferService offerService;

    private final CreditService creditService;

    @Operation(summary = "Get offers", description = "Get 4 prescoring variations of credit offers")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getOffers(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        List<LoanOfferDto> sortedLoanOffers = offerService.generateOffers(requestDto).stream()
                .sorted((o1, o2) -> o2.getRate().compareTo(o1.getRate()))
                .toList();

        return ResponseEntity.ok(sortedLoanOffers);
    }

    @Operation(summary = "Get credit", description = "Get credit offer with all parameters")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "422", description = "Business logic error")
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> getCredit(@Valid @RequestBody ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(creditService.createCredit(scoringDataDto));
    }
}
