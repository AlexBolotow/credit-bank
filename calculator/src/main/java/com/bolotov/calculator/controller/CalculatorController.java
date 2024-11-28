package com.bolotov.calculator.controller;

import com.bolotov.calculator.dto.LoanOfferDto;
import com.bolotov.calculator.dto.LoanStatementRequestDto;
import com.bolotov.calculator.service.interfaces.OfferService;
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

    @PostMapping("/offers")
    public ResponseEntity<?> getOffers(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        List<LoanOfferDto> sortedLoanOffers = offerService.generateOffers(requestDto).stream()
                .sorted((o1, o2) -> o2.getRate().compareTo(o1.getRate()))
                .toList();

        return ResponseEntity.ok(sortedLoanOffers);
    }
}
