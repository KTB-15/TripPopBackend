package com.kakaotech.back.vo;

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
}
