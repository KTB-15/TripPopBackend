package com.kakaotech.back.dto.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String role;
    private String name;
    private String username;
}
