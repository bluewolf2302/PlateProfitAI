package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.DemandPredictionRequestDto;
import com.plateprofit.ai.dto.DemandPredictionResponseDto;
import com.plateprofit.ai.service.AiPredictionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiPredictionService service;

    public AiController(AiPredictionService service) {
        this.service = service;
    }

    @PostMapping("/demand/predict")
    public DemandPredictionResponseDto predictDemand(@Valid @RequestBody DemandPredictionRequestDto request) {
        return service.predictDemand(request);
    }
}
