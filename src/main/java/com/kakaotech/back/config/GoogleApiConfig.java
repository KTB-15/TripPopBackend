package com.kakaotech.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class GoogleApiConfig {

    @Value("${google.maps.api-key}")
    private String apiKey;

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .requestFactory(
                        new JdkClientHttpRequestFactory(
                                HttpClient.newBuilder()
                                        .followRedirects(HttpClient.Redirect.NORMAL)
                                        .connectTimeout(Duration.ofSeconds(5))
                                        .build()
                        )
                )
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "image/*")
                .build();
    }
}
