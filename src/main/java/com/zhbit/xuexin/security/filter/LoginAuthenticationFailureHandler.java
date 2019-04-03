package com.zhbit.xuexin.security.filter;

import com.zhbit.xuexin.common.response.Result;
import com.zhbit.xuexin.security.common.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String errorMsg = "Login Failed: " + e.getMessage();
        Result errorResponse = new Result("ERROR", errorMsg, HttpStatus.UNAUTHORIZED);
        ResponseUtil.writeErrorReponse(httpServletResponse, errorResponse, HttpStatus.BAD_REQUEST);
    }
}
