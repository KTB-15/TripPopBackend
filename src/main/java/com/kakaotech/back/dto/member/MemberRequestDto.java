package com.kakaotech.back.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRequestDto {
    private String member_id;
    private String password;
    private String gender;
    private String ageGroup;
}

