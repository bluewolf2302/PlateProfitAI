package com.plateprofit.ai.dto;

import java.time.LocalDate;

public record DemandPredictionResponseDto(
        String dish,
        LocalDate targetDate,
        Integer predictedDemand,
        String method,
        boolean sufficientHistory,
        String message) { }
