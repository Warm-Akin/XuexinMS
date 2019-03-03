package com.zhbit.xuexin.controller.student;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xuexin/student")
public class StudentSelfController {

    @Autowired
    StudentService studentService;

    @GetMapping(value = "findByStudentNo/{studentNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllForPageable(@PathVariable("studentNo") String studentNo) {
        return ResponseUtil.success(studentService.findByStudentNo(studentNo));
    }
}
