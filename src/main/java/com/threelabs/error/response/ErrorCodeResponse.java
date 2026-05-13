package com.threelabs.error.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorCodeResponse {

    private LocalDateTime timestamp;

    @JsonIgnore
    private HttpStatus status;

    private Integer code;

    private String message;

    public ErrorCodeResponse(HttpStatus status, Integer code, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
