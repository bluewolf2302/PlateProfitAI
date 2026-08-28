package com.plateprofit.ai.service;

import com.plateprofit.ai.dto.DemandPredictionRequestDto;
import com.plateprofit.ai.dto.DemandPredictionResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiPredictionService {
    private final RestClient aiServiceClient;

    public AiPredictionService(RestClient aiServiceClient) {
        this.aiServiceClient = aiServiceClient;
    }

    public DemandPredictionResponseDto predictDemand(DemandPredictionRequestDto request) {
        return aiServiceClient.post()
                .uri("/api/ai/demand/predict")
                .body(request)
                .retrieve()
                .body(DemandPredictionResponseDto.class);
    }
}
