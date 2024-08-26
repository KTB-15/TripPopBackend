package com.kakaotech.back.dto.place;

import lombok.Builder;

import java.util.List;

@Builder
public record PlaceListResDto(
    List<PlaceResDto> places
){
}
