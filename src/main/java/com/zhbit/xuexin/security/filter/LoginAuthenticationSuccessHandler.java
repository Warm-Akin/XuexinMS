package com.zhbit.xuexin.security.filter;

import com.zhbit.xuexin.common.response.Result;
import com.zhbit.xuexin.security.common.JwtConfig;
import com.zhbit.xuexin.security.common.JwtToken;
import com.zhbit.xuexin.security.common.ResponseUtil;
import com.zhbit.xuexin.security.common.UserContext;
import com.zhbit.xuexin.security.constant.AuthenticationConstant;
import com.zhbit.xuexin.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        try {
            UserContext userContext = (UserContext) authentication.getPrincipal();
            JwtToken accessToken = jwtTokenUtil.createAccessToken(userContext);
            JwtToken refreshToken = jwtTokenUtil.createRefreshToken(userContext);
            // todo add refresh token
            Cookie tokenCookie = new Cookie(AuthenticationConstant.AUTHENTICATION_COOKIE, accessToken.getRawToken());
            tokenCookie.setMaxAge(jwtConfig.getTokenCookieMaxAge() * 60);
            Cookie refreshTokenCookie = new Cookie(AuthenticationConstant.REFRESH_AUTHENTICATION_COOKIE, refreshToken.getRawToken());
            refreshTokenCookie.setMaxAge(jwtConfig.getTokenRefreshCookieMaxAge() * 60);
            // todo add refresh cookie
            httpServletResponse.addCookie(tokenCookie);
            httpServletResponse.addCookie(refreshTokenCookie);
            Map<String, JwtToken> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", accessToken);
            tokenMap.put("refreshToken", refreshToken);

            Result successResponse = new Result("SUCCESS", tokenMap);
            ResponseUtil.writeSuccessResponse(httpServletResponse, successResponse);
        } catch (Exception e) {
            Result errorResponse = new Result("ERROR", e.getMessage(), HttpStatus.OK);
            ResponseUtil.writeErrorResponse(httpServletResponse, errorResponse, HttpStatus.OK);
        }
    }
}
