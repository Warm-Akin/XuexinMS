package com.zhbit.xuexin.controller.company;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/xuexin/company/resume")
public class CompanyResumeController {

    @Autowired
    StudentResumeService studentResumeService;

    @GetMapping("/findAll/{page}/{pageSize}")
    public ResponseEntity findAll(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentResumeService.findAll(page, pageSize));
    }
}
