package com.threelabs.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
public class ResponseDto<T> {

    private int code;
    private String message;
    private T data;
    private boolean isLogin;

    public ResponseDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.isLogin = checkLogin();
    }

    private boolean checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
