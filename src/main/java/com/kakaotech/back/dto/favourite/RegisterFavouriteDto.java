package com.kakaotech.back.dto.favourite;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RegisterFavouriteDto {
    private String memberId;
    private String placeId;
}
