package com.kakaotech.back.dto.favourite;

import jakarta.validation.constraints.NotNull;

public record FavouriteReqDto (
        @NotNull Long placeId,
        Boolean isFavourite
){
}
