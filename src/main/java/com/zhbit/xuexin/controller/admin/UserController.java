package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping(value = "/findActiveUserList/{page}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllActiveUsers(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(userService.findAllActiveUsers(page, pageSize));
    }

    @PostMapping(value = "/save/{organizationName}")
    public ResponseEntity save(@PathVariable("organizationName") String organizationName, @RequestBody User user) {
        userService.saveUser(user, organizationName);
        return ResponseUtil.success(HttpStatus.OK);
    }

}