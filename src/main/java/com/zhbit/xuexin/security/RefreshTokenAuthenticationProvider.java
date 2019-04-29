package com.zhbit.xuexin.security;

import com.zhbit.xuexin.common.response.Result;
import com.zhbit.xuexin.security.common.*;
import com.zhbit.xuexin.security.constant.AuthenticationConstant;
import com.zhbit.xuexin.security.exception.AuthenticationTokenException;
import com.zhbit.xuexin.security.util.JwtTokenUtil;
import com.zhbit.xuexin.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/xuexin")
public class RefreshTokenAuthenticationProvider {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    UserService userService;

    @RequestMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String tokenPayload = request.getHeader(AuthenticationConstant.JWT_REFRESH_TOKEN_HEADER);
            AccessToken refreshToken = new AccessToken(JwtTokenUtil.extract(tokenPayload));
//            AccessToken jwtToken = new AccessToken(TokenExtractor.extract(tokenPayload));
            Jws<Claims> claimsJws = jwtTokenUtil.parseClaims(jwtConfig.getTokenSigningKey(), refreshToken.getRawToken());
            List<String> scopes = claimsJws.getBody().get("scopes", List.class);
            if (null == scopes || scopes.size() == 0 || !AuthenticationConstant.ROLE_REFRESH.equals(scopes.get(0))) {
                throw new AuthenticationTokenException("Refresh Token Failed: Invalid JWT RefreshToken");
            }
            String username = claimsJws.getBody().getSubject();
            UserContext userContext = new UserContext();
            userContext.setUsername(username);
            userContext.setUser(userService.findByEmployNo(username));
            userContext.setAuthorities(getUserAuthorities(username));

//            String domainId = claimsJws.getBody().getSubject();
//            FwkUserContext userContext = new FwkUserContext();
//            userContext.setUserInfo(ldapHelper.searchUserInfo(domainId));
//            userContext.setUsername(domainId);
//            userContext.setAuthorities(getUserAuthorities(domainId));

            JwtToken accessToken = jwtTokenUtil.createAccessToken(userContext);
            Cookie cookie = new Cookie(AuthenticationConstant.AUTHENTICATION_COOKIE, accessToken.getRawToken());
            cookie.setMaxAge(jwtConfig.getTokenCookieMaxAge() * 60);
            response.addCookie(cookie);
            Result successResponse = new Result("REFRESH", accessToken);
            ResponseUtil.writeSuccessResponse(response,successResponse);
        } catch (Exception e) {
//            HttpStatus.OK
            Result errorResponse = new Result("ERROR", e.getMessage(), HttpStatus.UNAUTHORIZED);
            // HttpStatus.OK todo 参考LoginAuthenticationSuccessHandler
            ResponseUtil.writeErrorResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    private List<GrantedAuthority> getUserAuthorities(String username) {
        String role = userService.getUserTypeByUserName(username);
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(role));
        return auths;
    }

}
