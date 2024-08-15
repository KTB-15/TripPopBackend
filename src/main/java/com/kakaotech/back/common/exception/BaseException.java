package com.kakaotech.back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
