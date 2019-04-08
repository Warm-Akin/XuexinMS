package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.StudentCourseScoreDetailDto;
import com.zhbit.xuexin.model.StudentCourseScoreDetail;
import com.zhbit.xuexin.service.StudentCourseScoreDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/xuexin/security/admin/studentCourseDetail")
public class StudentCourseScoreDetailController {

    @Autowired
    StudentCourseScoreDetailService studentCourseScoreDetailService;

    @GetMapping(value = "/findAll/{page}/{pageSize}")
    public ResponseEntity findAllForPageable(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentCourseScoreDetailService.findAll(page, pageSize));
    }

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody StudentCourseScoreDetail studentCourseScoreDetail) {
        studentCourseScoreDetailService.save(studentCourseScoreDetail);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findDetailsByConditions(@RequestBody StudentCourseScoreDetailDto studentCourseScoreDetailDto) {
        return ResponseUtil.success(studentCourseScoreDetailService.findByConditions(studentCourseScoreDetailDto));
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity findAllActive() {
        return ResponseUtil.success(studentCourseScoreDetailService.findAllActive());
    }

}
