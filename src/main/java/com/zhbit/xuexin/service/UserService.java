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
                userType = "STUDENT";
                break;
            case "1":
                userType = "TEACHER";
                break;
            case "2":
                userType = "ADMIN";
                break;
            case "3":
                userType = "COMPANY";
                break;
            default:
                userType = "NONE";
        }
        return userType;
    }
}
