package com.kakaotech.back.service;


import com.kakaotech.back.common.exception.UnauthorizedException;
import com.kakaotech.back.dto.auth.LoginDto;
import com.kakaotech.back.dto.auth.TokenDto;
import com.kakaotech.back.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenDto signin(LoginDto loginDto, HttpServletRequest request) {
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

        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenDto refreshAccessToken(@RequestBody String refreshToken) {
        String newAccessToken = tokenProvider.refreshAccessToken(refreshToken);

        if (newAccessToken == null) {
            throw new UnauthorizedException("refresh access token expired");
        }

        return new TokenDto(newAccessToken, refreshToken);
    }
}
