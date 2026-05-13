package com.threelabs.error.exception;

import lombok.Getter;

@Getter
public class CustomErrorCodeException extends RuntimeException {

    private final Integer code;

    public CustomErrorCodeException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
