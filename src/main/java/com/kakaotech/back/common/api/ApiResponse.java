package com.kakaotech.back.common.api;

import com.kakaotech.back.common.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private ErrorMessage errorMessage;

    public static <T> ApiResponse<T> success (T data) {
        return new ApiResponse<T>(data, null);
    }

    public static ApiResponse<ErrorMessage> fail (ErrorMessage error){
        return new ApiResponse<>(null, error);
    }
}
