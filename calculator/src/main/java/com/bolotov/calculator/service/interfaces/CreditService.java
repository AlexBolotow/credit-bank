package com.bolotov.calculator.service.interfaces;

import com.bolotov.calculator.dto.CreditDto;
import com.bolotov.calculator.dto.ScoringDataDto;

public interface CreditService {

    CreditDto createCredit(ScoringDataDto scoringDataDto);
}
