package com.kakaotech.back.dto.place;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record PlaceListReqDto(
        @NotEmpty(message = "EMPTY ARRAY")
        @Size(max = 10)
        List<@Min(value = 1, message = "SHOULD BE ABOVE THAN 0") Long> places
) {
}
