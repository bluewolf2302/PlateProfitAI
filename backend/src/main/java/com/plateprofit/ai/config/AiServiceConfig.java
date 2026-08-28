package com.plateprofit.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiServiceConfig {
    @Bean
    RestClient aiServiceRestClient(@Value("${AI_SERVICE_URL:http://localhost:8001}") String aiServiceUrl) {
        return RestClient.builder().baseUrl(aiServiceUrl).build();
    }
}
