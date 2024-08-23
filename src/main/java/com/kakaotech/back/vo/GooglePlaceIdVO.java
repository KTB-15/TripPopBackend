package com.kakaotech.back.vo;

import lombok.Builder;

import java.util.List;

/*
* 좌표로부터 얻는 Google Place 장소의 Id
* 예시
{
  "places": [
    {
      "name": "places/bla-bla"
    }
  ]
}
* */
@Builder
public record GooglePlaceIdVO(List<Place> places) {
    public static record Place(String name) {
    }
}
