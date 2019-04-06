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
        String errorMsg = "登录失败: " + e.getMessage();
//        Result errorResponse = new Result("ERROR", errorMsg);
//        ErrorResponse errorResponse = ErrorResponse.of("ERROR", errorMsg, HttpStatus.UNAUTHORIZED);

        Result errorResponse = new Result("ERROR", errorMsg, HttpStatus.UNAUTHORIZED);
        ResponseUtil.writeErrorResponse(httpServletResponse, errorResponse, HttpStatus.OK);
    }
}
