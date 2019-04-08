package com.zhbit.xuexin.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationTokenException extends AuthenticationException {

    public AuthenticationTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationTokenException(String msg) {
        super(msg);
    }
}
