package com.zhbit.xuexin.controller;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/xuexin/admin/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping(value = "/findAll/{page}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllForPageable(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(courseService.findAll(page, pageSize));
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity findAll() {
        return ResponseUtil.success(courseService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Course course) {
        courseService.handleSave(course);
        return ResponseUtil.success(HttpStatus.OK);
    }
}
