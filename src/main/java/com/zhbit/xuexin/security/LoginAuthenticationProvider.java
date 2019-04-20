package com.zhbit.xuexin.security;

import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.security.common.UserContext;
import com.zhbit.xuexin.service.LoginService;
import com.zhbit.xuexin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

//    @Autowired
//    UserDetailsService userDetailsService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        User user = loginService.validateUserLogin(userName, password);
        if (null == user)
            throw new BadCredentialsException("用户名或密码错误");
        else {
            // user is not null
            // trim '.' -> status: 0.0 / 1.0
            if (String.valueOf(user.getStatus()).substring(0, 1).equals("1"))
                throw new BadCredentialsException("该用户已被禁用，请联系管理员");
        }
        UserContext userContext = new UserContext();
        userContext.setUser(user);
        userContext.setUsername(userName);
        userContext.setAuthorities(getUserAuthorities(userName));
        return new UsernamePasswordAuthenticationToken(userContext, password, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    // todo get the real authentication role
    private List<GrantedAuthority> getUserAuthorities(String username) {
        String role = userService.getUserTypeByUserName(username);
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(role));
        return auths;
    }
}
