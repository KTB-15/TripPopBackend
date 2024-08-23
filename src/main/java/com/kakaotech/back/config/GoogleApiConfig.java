package com.kakaotech.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GoogleApiConfig {

    @Value("${google.maps.api-key}")
    private String apiKey;

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-Goog-Api-Key", apiKey)
                .build();
    }
}
