package com.kakaotech.back.controller;

import com.kakaotech.back.dto.auth.LoginDto;
import com.kakaotech.back.dto.auth.TokenDto;
import com.kakaotech.back.entity.RefreshToken;
import com.kakaotech.back.jwt.JwtFilter;
import com.kakaotech.back.jwt.TokenProvider;
import com.kakaotech.back.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
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
        System.out.println(refreshToken);

        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @GetMapping("/jwt-token")
    public ResponseEntity<TokenDto> getJwtToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;
        String refreshToken = null;

        // 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }

                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 토큰이 없는 경우 오류 응답 반환
        if (accessToken == null || refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 쿠키 만료
        clearCookies(response);

        // 토큰 DTO 생성
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        // 응답 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccessToken());

        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }

    public void clearCookies(HttpServletResponse response) {
        // 만료된 쿠키를 클라이언트로 전송
        Cookie expiredAccessTokenCookie = new Cookie("accessToken", null);
        expiredAccessTokenCookie.setPath("/");
        expiredAccessTokenCookie.setHttpOnly(true);
        expiredAccessTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(expiredAccessTokenCookie);

        Cookie expiredRefreshTokenCookie = new Cookie("refreshToken", null);
        expiredRefreshTokenCookie.setPath("/");
        expiredRefreshTokenCookie.setHttpOnly(true);
        expiredRefreshTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(expiredRefreshTokenCookie);
    }
}