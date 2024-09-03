package com.kakaotech.back.dto.oauth;

import com.kakaotech.back.entity.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserDto {
    private Set<Authority> authorities;
    private String name;
    private String username;
}
