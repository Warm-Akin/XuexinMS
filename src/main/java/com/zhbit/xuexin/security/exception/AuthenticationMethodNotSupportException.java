package com.zhbit.xuexin.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthenticationMethodNotSupportException extends AuthenticationServiceException {
    public AuthenticationMethodNotSupportException(String message) {
        super(message);
    }

    public AuthenticationMethodNotSupportException(String msg, Throwable t) {
        super(msg, t);
    }
}
