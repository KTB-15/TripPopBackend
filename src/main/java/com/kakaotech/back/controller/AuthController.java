package com.kakaotech.back.controller;

import com.kakaotech.back.dto.auth.LoginDto;
import com.kakaotech.back.dto.auth.TokenDto;
import com.kakaotech.back.entity.RefreshToken;
import com.kakaotech.back.jwt.JwtFilter;
import com.kakaotech.back.jwt.TokenProvider;
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
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {

        // AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getMemberId(), loginDto.getPassword());

        // 인증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Access Token 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        // Refresh Token 생성
        String refreshToken = tokenProvider.createRefreshToken(authentication, request);

        // 응답 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshAccessToken(@RequestBody String refreshToken) {
        // refresh token의 유효성 검사 및 새로운 access token 발급
        String newAccessToken = tokenProvider.refreshAccessToken(refreshToken);

        if (newAccessToken == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new TokenDto(newAccessToken, refreshToken), HttpStatus.OK);
    }
}