package com.kakaotech.back.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberResponseDto {
    private String id;
    private String memberId;
    private String gender;
    private Integer ageGroup;
}

