package com.kakaotech.back.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 로그인의 경우 pass
        if (requestURI.startsWith("/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            // Access Token이 만료된 경우 처리
            if (StringUtils.hasText(jwt)) {
                String refreshToken = httpServletRequest.getHeader("Refresh-Token");
                if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
                    String newAccessToken = tokenProvider.refreshAccessToken(refreshToken);

                    if (newAccessToken != null) {
                        // Set new access token in response headers
                        httpServletResponse.setHeader(AUTHORIZATION_HEADER, "Bearer " + newAccessToken);

                        Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Access token 만료됨. 새로운 access token을 발급했습니다, uri: {}", requestURI);
                    } else {
                        // Refresh Token이 유효하지 않거나 없는 경우
                        // TODO: Refresh token과 access token 재발급을 위해서 front를 로그인 페이지로 이동시켜야 함.
                        logger.debug("유효한 refresh token이 없습니다, uri: {}", requestURI);
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is invalid or expired.");
                        return;
                    }
                } else {
                    // Refresh Token이 없거나 유효하지 않음
                    // TODO: Refresh token 요청 (헤더에 담아서)
                    logger.debug("Refresh token이 없습니다, uri: {}", requestURI);
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is missing or invalid.");
                    return;
                }
            } else {
                // 인증 헤더가 없을 경우
                logger.debug("Authorization header가 없습니다, uri: {}", requestURI);
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing.");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
