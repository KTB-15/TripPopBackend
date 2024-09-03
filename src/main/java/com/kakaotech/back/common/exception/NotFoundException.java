package com.kakaotech.back.common.exception;

public class NotFoundException extends RuntimeException {
    public static String messageWithInfo(String info){
        return "존재하지 않습니다: " + info;
    }
    public NotFoundException() {}
    public NotFoundException(String message) {super(message);}
}
