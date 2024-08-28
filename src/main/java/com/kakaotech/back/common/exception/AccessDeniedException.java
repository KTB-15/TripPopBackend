package com.kakaotech.back.common.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {}
    public AccessDeniedException(String message) {
        super(message);
    }
}
