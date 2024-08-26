package com.kakaotech.back.dto.place;

import lombok.Builder;

@Builder
public record PlaceResDto(
        Long placeId,
        byte[] image
) {
}
