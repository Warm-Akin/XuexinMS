package com.zhbit.xuexin.security.filter;

import com.zhbit.xuexin.common.response.Result;
import com.zhbit.xuexin.security.common.ResponseUtil;
import com.zhbit.xuexin.security.constant.AuthenticationConstant;
import com.zhbit.xuexin.security.exception.AuthenticationMethodNotSupportException;
import com.zhbit.xuexin.security.exception.AuthenticationTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String errorMsg;
        if (e instanceof BadCredentialsException) {
            errorMsg = AuthenticationConstant.LOGIN_FAILED + e.getMessage();
        } else if (e instanceof AuthenticationMethodNotSupportException) {
            errorMsg = e.getMessage();
        } else if (e instanceof AuthenticationTokenException) {
            errorMsg = "token Expired";
        } else {
            errorMsg = AuthenticationConstant.AUTHENTICATION_FAILED + e.getMessage();
        }
        //        Result errorResponse = new Result("ERROR", errorMsg);
        //        ErrorResponse errorResponse = ErrorResponse.of("ERROR", errorMsg, HttpStatus.UNAUTHORIZED);

        Result errorResponse = new Result("ERROR", errorMsg, HttpStatus.UNAUTHORIZED);
        ResponseUtil.writeErrorResponse(httpServletResponse, errorResponse, HttpStatus.UNAUTHORIZED); // HttpStatus.OK
    }
}
