package com.kakaotech.back.jwt;

import com.kakaotech.back.entity.Authority;
import com.kakaotech.back.entity.Member;
import com.kakaotech.back.entity.RefreshToken;
import com.kakaotech.back.repository.AuthorityRepository;
import com.kakaotech.back.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            MemberRepository memberRepository, AuthorityRepository authorityRepository, RedisTemplate<String, Object> redisTemplate, @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds) {
        this.memberRepository = memberRepository;
        this.authorityRepository = authorityRepository;
        this.redisTemplate = redisTemplate;
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliSeconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    @Transactional
    public String createRefreshToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        // Redis에 저장
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .id(authentication.getName())
                .refreshToken(refreshToken)
                .authorities(Collections.singletonList(authorities))
                .build();

        redisTemplate.opsForValue().set(
                "refreshToken:" + authentication.getName(),
                refreshTokenObj,
                refreshTokenValidityInMilliseconds,
                TimeUnit.MILLISECONDS);

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

            if ("google".equalsIgnoreCase(registrationId)) {
                // PostgreSQL에 저장
                Authority authority = Authority.builder()
                        .authorityName("ROLE_USER")
                        .build();
                if(!authorityRepository.existsByAuthorityName("ROLE_USER")) authorityRepository.save(authority);

                if(memberRepository.existsByMemberId(authentication.getName())) { return refreshToken; }

                Member member = Member.builder()
                        .memberId(authentication.getName())
                        .authorities(Collections.singleton(authority))
                        .activated(true)
                        .build();
                memberRepository.save(member);
            }
        }

        return refreshToken;
    }

    public String refreshAccessToken(String refreshToken) {
        // 요청으로 들어온 refresh token을 파싱하여 username 추출
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();
        } catch (JwtException e) {
            // JWT 토큰이 유효하지 않을 경우
            logger.info("Invalid JWT token: {}", e.getMessage());
            return null;
        }

        if (claims == null) {
            // 클레임이 없으면 무효한 토큰으로 간주
            return null;
        }

        // 클레임에서 사용자 ID 추출
        String username = claims.getSubject();

        // Redis에서 저장된 refresh token을 조회
        RefreshToken storedToken = (RefreshToken) redisTemplate.opsForValue().get("refreshToken:" + username);

        // Redis에 저장된 refresh token과 요청으로 들어온 refresh token 비교
        if (storedToken != null && storedToken.getRefreshToken().equals(refreshToken)) {
            // 새로운 access token 생성
            return createAccessTokenWithClaims(claims);
        } else {
            // refresh token이 유효하지 않거나 존재하지 않을 때 null을 반환
            // TODO: 재로그인
            return null;
        }
    }

    private String createAccessTokenWithClaims(Claims claims) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY, String.class).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
