package com.kakaotech.back.dto.place;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PlaceReqDto(@NotNull(message = "should not be null") Long placeId) {
}
