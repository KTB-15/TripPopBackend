package com.kakaotech.back.controller;

import com.kakaotech.back.dto.auth.LoginDto;
import com.kakaotech.back.dto.auth.TokenDto;
import com.kakaotech.back.entity.RefreshToken;
import com.kakaotech.back.jwt.JwtFilter;
import com.kakaotech.back.jwt.TokenProvider;
import com.kakaotech.back.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        TokenDto tokenDto = authService.signin(loginDto, request);

        // 응답 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshAccessToken(@RequestBody String refreshToken) {
        TokenDto tokenDto = authService.refreshAccessToken(refreshToken);

        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }
}