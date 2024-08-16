package com.kakaotech.back.dto.favourite;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterMemberTravelStylesDto {
    private String travelStyle1;
    private String travelStyle2;
    private String travelStyle3;
    private String travelStyle4;
    private String travelStyle5;
    private String travelStyle6;
    private String travelStyle7;
    private String travelStyle8;
}
