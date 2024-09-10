package com.kakaotech.back.common.exception;

public class NotFoundException extends RuntimeException {
    public static String messageWithInfo(Object info){
        return "존재하지 않습니다: " + info.toString();
    }
    public NotFoundException() {}
    public NotFoundException(String message) {super(message);}
}
