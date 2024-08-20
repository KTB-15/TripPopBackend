package com.kakaotech.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/member/**").permitAll() // /member 경로에 대한 접근 허용
                .anyRequest().authenticated() // 그 외의 요청은 인증 필요
                );


        return http.build(); // Spring Security 5.0 이상에서는 build()를 호출하여 SecurityFilterChain 객체를 생성합니다.
    }

}
