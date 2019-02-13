package com.zhbit.xuexin.controller;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/xuexin/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<Student> studentList = studentService.findAll();
        return ResponseUtil.success(studentList);
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Student student) {
        List<Student> studentList = studentService.save(student);
        return ResponseUtil.success(studentList);
    }


}
