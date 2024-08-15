package com.kakaotech.back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NO USER"),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO PLACE"),
    FAVOURITE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO FAVOURITE");


    private final HttpStatus httpStatus;
    private final String message;
}
