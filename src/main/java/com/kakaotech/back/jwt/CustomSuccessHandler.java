package com.kakaotech.back.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotech.back.dto.auth.TokenDto;
import com.kakaotech.back.dto.oauth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // JWT 토큰 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        response.addCookie(generateokenCookie("accessToken", accessToken));
        response.addCookie(generateokenCookie("refreshToken", refreshToken));

        logger.debug("accessToken: " + accessToken);
        logger.debug("refreshToken: " + refreshToken);

        response.sendRedirect("http://localhost:5173/");
    }

    public Cookie generateokenCookie(String key, String token) {
       Cookie cookie = new Cookie(key, token);
       cookie.setPath("/");
       cookie.setHttpOnly(true);
       return cookie;
    }

}