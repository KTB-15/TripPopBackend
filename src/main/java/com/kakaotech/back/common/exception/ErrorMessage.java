package com.kakaotech.back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // 404
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "ALREADY EXISTS MEMBER"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NO USER"),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO PLACE"),
    FAVOURITE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO FAVOURITE"),

    // 502
    GET_PLACE_IMAGE_FAILED(HttpStatus.BAD_GATEWAY, "FAILED TO GET PLACE IMAGE FROM GOOGLE");
    private final HttpStatus httpStatus;
    private final String message;
}
