package com.kakaotech.back.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException{
    private final ErrorMessage errorMessage;

    @Override
    public String getMessage() {
        return errorMessage.getMessage();
    }

    public Integer getStatus() {
        return errorMessage.getHttpStatus().value();
    }
}
