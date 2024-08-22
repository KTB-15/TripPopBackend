package com.kakaotech.back.environment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnvironmentTest {

    @Value("${google.maps.api-key}")
    private String googleApiKey;

    @Test
    @DisplayName("설정 정보 파일 읽기")
    void testLogEnvProp() {
        System.out.println(googleApiKey);
    }
}
