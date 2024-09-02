package com.kakaotech.back.vo;

import com.kakaotech.back.entity.Member;
import lombok.Builder;

// AI에 전송할 요청 본문
@Builder
public record TravelerInfoVO(
        String gender,
        Integer ageGroup,
        Integer travelStyle1,
        Integer travelStyle2,
        Integer travelStyle4,
        Integer travelStyle5,
        Integer travelStyle6,
        Integer travelStyle7,
        Integer travelStyle8,
        String sidoName,
        String sggName
) {
    public static TravelerInfoVO from(Member member) {
        return TravelerInfoVO.builder()
                .gender(member.getGender().name())
                .travelStyle1(member.getTravelStyle1())
                .travelStyle2(member.getTravelStyle2())
                .travelStyle4(member.getTravelStyle4())
                .travelStyle5(member.getTravelStyle5())
                .travelStyle6(member.getTravelStyle6())
                .travelStyle7(member.getTravelStyle7())
                .travelStyle8(member.getTravelStyle8())
                .sidoName(member.getTravelLikeSGG().getSidoName())
                .sggName(member.getTravelLikeSGG().getSggName())
                .build();
    }
}
