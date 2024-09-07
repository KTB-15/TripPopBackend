package com.kakaotech.back.dto.favourite;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RegisterFavouriteDto {

    @NotBlank
    private String memberId;
    @NotBlank
    private Long placeId;
}
