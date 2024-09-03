package com.kakaotech.back.dto;

import com.kakaotech.back.entity.Place;
import lombok.Builder;

@Builder
public record RecommendResDto (
        Long placeId,
        String areaName,
        String roadName,
        byte[] placeImage,
        Boolean isFavourite
){
    public static RecommendResDto of(Place place, byte[] placeImage, Boolean isFavourite){
        return RecommendResDto.builder()
                .placeId(place.getId())
                .areaName(place.getAreaName())
                .roadName(place.getRoadName())
                .placeImage(placeImage)
                .isFavourite(isFavourite)
                .build();
    }
}
