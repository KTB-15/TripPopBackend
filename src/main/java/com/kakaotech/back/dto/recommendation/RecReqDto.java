package com.kakaotech.back.dto.recommendation;

import com.kakaotech.back.entity.Member;
import com.kakaotech.back.vo.TravelerInfoVO;
import lombok.Builder;

@Builder
public record RecReqDto (
        String GENDER,
        int AGE_GRP,
        int TRAVEL_STYL_1,
        int TRAVEL_STYL_2,
        int TRAVEL_STYL_4,
        int TRAVEL_STYL_5,
        int TRAVEL_STYL_6,
        int TRAVEL_STYL_7,
        int TRAVEL_STYL_8,
        String SIDO,
        String GUNGU
){

    public static RecReqDto from(Member member) {
        return RecReqDto.builder()
                .GENDER(member.getGender().name())
                .TRAVEL_STYL_1(member.getTravelStyle1())
                .TRAVEL_STYL_2(member.getTravelStyle2())
                .TRAVEL_STYL_4(member.getTravelStyle4())
                .TRAVEL_STYL_5(member.getTravelStyle5())
                .TRAVEL_STYL_6(member.getTravelStyle6())
                .TRAVEL_STYL_7(member.getTravelStyle7())
                .TRAVEL_STYL_8(member.getTravelStyle8())
                .SIDO("강원도")
                .GUNGU("속초시")
                .build();
    }
}
