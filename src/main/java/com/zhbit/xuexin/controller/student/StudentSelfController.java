package com.zhbit.xuexin.controller.student;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/xuexin/security/student")
public class StudentSelfController {

    @Autowired
    StudentService studentService;

    @GetMapping(value = "findByStudentNo/{studentNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllForPageable(@PathVariable("studentNo") String studentNo) {
        return ResponseUtil.success(studentService.findByStudentNo(studentNo));
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Student student) {
        studentService.handleSave(student);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity updatePassword(@RequestBody PasswordDto passwordDto) {
        studentService.updatePassword(passwordDto);
        return ResponseUtil.success(HttpStatus.OK);
    }
}
