package com.kakaotech.back.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberResponseDto {
    private String id;
    private String gender;
    private Integer ageGroup;
}

