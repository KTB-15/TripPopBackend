package com.kakaotech.back.common;

import com.kakaotech.back.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    ProblemDetail handleNotFoundException(NotFoundException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    ProblemDetail handleAlreadyExistsException(AlreadyExistsException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(GoogleApiException.class)
    ProblemDetail handleExternalApiException(GoogleApiException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
    }
}
