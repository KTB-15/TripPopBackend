package com.kakaotech.back.dto.oauth;

import com.kakaotech.back.entity.Gender;

import java.util.Calendar;
import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {

        return "naver";
    }

    @Override
    public String getProviderId() {

        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        return attribute.get("id").toString();
    }

    public Gender getGender() {
        Gender gender = null;
        String temp = attribute.get("gender").toString();
        if(temp.equals("M")) gender = Gender.MALE;
        else if(temp.equals("F")) gender = Gender.FEMALE;
        return gender;
    }

    public int getAge() {
        // 현재 년도 가져오기
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // 태어난 해 가져오기
        int birthYear = Integer.parseInt(attribute.get("birthyear").toString());

        // 나이 계산
        int age = currentYear - birthYear;

        if (age < 10) {
            return 0;
        } else if (age < 20) {
            return 10;
        } else if (age < 30) {
            return 20;
        } else if (age < 40) {
            return 30;
        } else if (age < 50) {
            return 40;
        } else if (age < 60) {
            return 50;
        } else if (age < 70) {
            return 60;
        } else if (age < 80) {
            return 70;
        } else {
            return 80; // 80대 이상
        }
    }
}