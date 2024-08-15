package com.kakaotech.back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MemberException extends BaseException{
    public MemberException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
