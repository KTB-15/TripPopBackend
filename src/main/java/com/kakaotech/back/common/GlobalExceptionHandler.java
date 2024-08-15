package com.kakaotech.back.common;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.common.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MemberException.class, PlaceException.class, FavouriteException.class})
    public ResponseEntity<ApiResponse<ErrorMessage>> handleCustomException(BaseException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.fail(e.getErrorMessage()));
    }
}
