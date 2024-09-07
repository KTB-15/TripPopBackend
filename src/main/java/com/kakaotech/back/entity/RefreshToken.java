package com.kakaotech.back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

    private String id;

    @JsonProperty("authorities")
    private List<String> authorities; // 문자열 리스트로 변경

    @Indexed
    private String refreshToken;
}
