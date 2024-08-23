package com.kakaotech.back.common;

import com.kakaotech.back.common.api.ApiResponse;
import com.kakaotech.back.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MemberException.class, PlaceException.class, FavouriteException.class})

    public ResponseEntity<ApiResponse<ErrorMessage>> handleCustomException(BaseException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.fail(e.getErrorMessage()));
    }

    // 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e){
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            var fieldError = new HashMap<>();
            fieldError.put("message", error.getDefaultMessage());
            fieldError.put("input", error.getRejectedValue());
            errors.put(error.getField(), fieldError);
        });

        response.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
