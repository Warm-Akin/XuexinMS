package com.zhbit.xuexin.service;

import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String getUserTypeByUserName(String username) {
        User user = userRepository.findByEmployNo(username);
        String userType = "";
        switch (user.getUserType()) {
            case "0":
                userType = "ROLE_STUDENT";
                break;
            case "1":
                userType = "ROLE_TEACHER";
                break;
            case "2":
                userType = "ROLE_ADMIN";
                break;
            case "3":
                userType = "ROLE_COMPANY";
                break;
            default:
                userType = "ROLE_NONE";
        }
        return userType;
    }
}
