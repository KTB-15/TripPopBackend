package com.kakaotech.back.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberRequestDto {
    private String memberId;
    private String password;
    private String gender;
    private String ageGroup;
}

