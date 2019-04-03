package com.zhbit.xuexin.controller;

import com.zhbit.xuexin.common.constant.HttpCode;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/xuexin/user")
public class LoginController {

//    @Autowired
//    LoginService loginService;
//
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody User user) {
//        User loginUser = loginService.checkLogin(user);
//        return ResponseUtil.success(loginUser);
//    }
}