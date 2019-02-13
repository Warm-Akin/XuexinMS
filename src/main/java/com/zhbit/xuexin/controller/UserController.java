package com.zhbit.xuexin.controller;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/xuexin/ahn")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/findAllUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUsers () {
        List<User> userList = userService.findAll();
        return ResponseUtil.success(userList.get(0));
    }

}