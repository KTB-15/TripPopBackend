package com.kakaotech.back.dto.member;

import com.kakaotech.back.entity.Member;
import lombok.Builder;

@Builder
public record MemberSurveyDto(
        Integer travelStyle1,
        Integer travelStyle2,
        Integer travelStyle3,
        Integer travelStyle4,
        Integer travelStyle5,
        Integer travelStyle6,
        Integer travelStyle7,
        Integer travelStyle8
) {
    public static MemberSurveyDto from(Member member) {
        return MemberSurveyDto.builder()
                .travelStyle1(member.getTravelStyle1())
                .travelStyle2(member.getTravelStyle2())
                .travelStyle3(member.getTravelStyle3())
                .travelStyle4(member.getTravelStyle4())
                .travelStyle5(member.getTravelStyle5())
                .travelStyle6(member.getTravelStyle6())
                .travelStyle7(member.getTravelStyle7())
                .travelStyle8(member.getTravelStyle8())
                .build();
    }
}
