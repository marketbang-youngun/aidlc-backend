package com.threelabs.error.Handler;

import com.threelabs.error.exception.CustomErrorCodeException;
import com.threelabs.error.response.ErrorCodeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorExceptionHandler {

    @ExceptionHandler(CustomErrorCodeException.class)
    public ResponseEntity<ErrorCodeResponse> handleCustomErrorCodeException(CustomErrorCodeException ex) {
        ErrorCodeResponse response = new ErrorCodeResponse(
                HttpStatus.BAD_REQUEST,
                ex.getCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
