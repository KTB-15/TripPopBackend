package com.kakaotech.back.common;

import com.kakaotech.back.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandlerAdvice {
    @ExceptionHandler({MemberException.class, PlaceException.class, FavouriteException.class})
    ProblemDetail handleCustomException(BaseException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
