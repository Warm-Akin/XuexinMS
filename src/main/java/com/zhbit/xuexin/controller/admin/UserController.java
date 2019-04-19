package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/xuexin/security/admin/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/updatePassword")
    public ResponseEntity updatePassword (@RequestBody PasswordDto passwordDto) {
        userService.updatePassword(passwordDto);
        return ResponseUtil.success(HttpStatus.OK);
    }

}