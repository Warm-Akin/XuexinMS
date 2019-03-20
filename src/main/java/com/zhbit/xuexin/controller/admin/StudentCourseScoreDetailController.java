package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.service.StudentCourseScoreDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xuexin/admin/studentCourseDetail")
public class StudentCourseScoreDetailController {

    @Autowired
    StudentCourseScoreDetailService studentCourseScoreDetailService;

    @GetMapping(value = "/findAll/{page}/{pageSize}")
    public ResponseEntity findAllForPageable(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentCourseScoreDetailService.findAll(page, pageSize));
    }

}
