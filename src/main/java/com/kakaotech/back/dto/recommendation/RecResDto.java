package com.kakaotech.back.dto.recommendation;

import lombok.Builder;

import java.util.List;

@Builder
public record RecResDto(List<String> recommended_places) {
}
