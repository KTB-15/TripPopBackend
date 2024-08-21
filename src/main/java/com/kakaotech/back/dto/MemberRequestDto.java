package com.kakaotech.back.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRequestDto {
    private String id;
    private String password;
    private String gender;
    private String ageGroup;
}

