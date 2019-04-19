package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.StudentDto;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/xuexin/security/admin/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping(value = "/findAll/{page}/{pageSize}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllForPageable(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentService.findAll(page, pageSize));
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity findAll() {
        return ResponseUtil.success(studentService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Student student) {
        studentService.handleSave(student);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity uploadStudentList(MultipartFile file) {
        studentService.uploadStudentList(file);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findStudentsByConditions(@RequestBody StudentDto studentDto) {
        return ResponseUtil.success(studentService.findByConditions(studentDto));
    }

}
