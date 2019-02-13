package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public Boolean checkLogin(User user) {
        if (user != null && !StringUtils.isEmpty(user.getEmployName()) && !StringUtils.isEmpty(user.getPassword()) ) {
            String userName = user.getEmployName();
            String password = SecurityUtil.GetMD5Code(user.getPassword());
            User currentUser = userRepository.findByEmployNameAndPassword(userName, password);
            if (currentUser != null) {
                // 验证成功，获取用户的权限

                return true;
            }
            return false;
        }
        return false;
    }
}
