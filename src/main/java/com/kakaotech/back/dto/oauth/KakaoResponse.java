package com.kakaotech.back.dto.oauth;

import com.kakaotech.back.entity.Gender;

import java.util.Calendar;
import java.util.Map;

public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attribute1;
    private final Map<String, Object> attribute2;

    public KakaoResponse(Map<String, Object> attribute1, Map<String, Object> attribute2) {
        this.attribute1 = attribute1;
        this.attribute2 = (Map<String, Object>) attribute2.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute1.get("id").toString();
    }

    @Override
    public String getEmail() {

        return attribute2.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute1.get("id").toString();
    }

    public Gender getGender() {
        if(attribute2.get("gender").toString().equals("male")) return Gender.MALE;
        else if(attribute2.get("gender").toString().equals("female")) return Gender.FEMALE;
        return Gender.MALE;
    }

    public int getAge() {
        return Integer.parseInt(attribute2.get("age_range").toString().substring(0, 2));
    }
}