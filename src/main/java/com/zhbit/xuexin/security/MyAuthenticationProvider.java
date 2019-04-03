package com.zhbit.xuexin.security;

import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.security.common.UserContext;
import com.zhbit.xuexin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

//    @Autowired
//    UserDetailsService userDetailsService;

    @Autowired
    LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        // todo check user login info
        User user = loginService.validateUserLogin(userName, password);
        if (null == user)
            throw new CustomException(ResultEnum.AccountInvalidException.getMessage(), ResultEnum.AccountInvalidException.getCode());
        UserContext userContext = new UserContext();
        userContext.setUser(user);
        userContext.setUsername(userName);
        // todo get user's authority
        userContext.setAuthorities(getUserAuthorities(userName));
        return new UsernamePasswordAuthenticationToken(userContext, password, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<GrantedAuthority> getUserAuthorities(String username) {
        String role = "TESTER";
        if ("test1".equals(username))
            role = "ADMIN";
        // get user roles by username
//        ESGAdmin esgAdmin = esgAdminService.checkLoginInfo(username);
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(role));
//        auths.add(new SimpleGrantedAuthority(esgAdmin.getType()));
        return auths;
    }
}
