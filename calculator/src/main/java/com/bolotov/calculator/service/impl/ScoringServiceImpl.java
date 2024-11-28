package com.bolotov.calculator.service.impl;

import com.bolotov.calculator.dto.EmploymentDto;
import com.bolotov.calculator.dto.ScoringDataDto;
import com.bolotov.calculator.enums.Gender;
import com.bolotov.calculator.exception.ScoringException;
import com.bolotov.calculator.service.interfaces.PrescoringService;
import com.bolotov.calculator.service.interfaces.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private final PrescoringService prescoringService;

    @Override
    public BigDecimal calculateRate(ScoringDataDto scoringDataDto) {
        BigDecimal rate = prescoringService.calculateRate(scoringDataDto.getIsInsuranceEnabled(),
                scoringDataDto.getIsSalaryClient());
        EmploymentDto employmentDto = scoringDataDto.getEmployment();

        switch (employmentDto.getEmploymentStatus()) {
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(0.02));
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(0.01));
            case UNEMPLOYED -> throw new ScoringException("Unemployed client");
        }

        BigDecimal twoYearsSalary = employmentDto.getSalary().multiply(BigDecimal.valueOf(24));
        if (scoringDataDto.getAmount().compareTo(twoYearsSalary) > 0) {
            throw new ScoringException("Not enough client salary");
        }

        if (employmentDto.getWorkExperienceTotal() < 18 || employmentDto.getWorkExperienceCurrent() < 3) {
            throw new ScoringException("Not enough client work experience");
        }

        switch (employmentDto.getPosition()) {
            case MIDDLE_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(0.02));
            case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        int fullYears = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        if (fullYears < 20 || fullYears > 65) {
            throw new ScoringException("Client age is out of range");
        }

        if (scoringDataDto.getGender() == Gender.FEMALE && fullYears >= 32 && fullYears < 60) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        if (scoringDataDto.getGender() == Gender.MALE && fullYears >= 30 && fullYears < 55) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        if (scoringDataDto.getGender() == Gender.NON_BINARY) {
            rate = rate.add(BigDecimal.valueOf(0.07));
        }

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(0.03));
            case DIVORCED -> rate = rate.add(BigDecimal.valueOf(0.01));
        }

        return rate;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(ScoringDataDto scoringDataDto, BigDecimal rate) {
        return prescoringService.calculateMonthlyPayment(scoringDataDto.getAmount(), rate, scoringDataDto.getTerm());
    }

    @Override
    public BigDecimal calculatePsk(ScoringDataDto scoringDataDto, BigDecimal rate) {
        BigDecimal creditAmount = scoringDataDto.getAmount();
        BigDecimal paymentsAmount = calculateMonthlyPayment(scoringDataDto, rate)
                .multiply(BigDecimal.valueOf(scoringDataDto.getTerm()));

        double termInYears = (double) scoringDataDto.getTerm() / 12;
        BigDecimal psk = ((paymentsAmount.divide(creditAmount, 10, RoundingMode.HALF_EVEN)).subtract(BigDecimal.ONE))
                .divide(new BigDecimal(termInYears), 3, RoundingMode.HALF_EVEN);

        return psk;
    }
}
