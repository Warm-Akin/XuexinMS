package com.zhbit.xuexin.controller.student;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.service.ResumeTemplateService;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/xuexin/security/student/resume")
public class StudentResumeController {

    @Autowired
    StudentResumeService studentResumeService;

    @Autowired
    ResumeTemplateService resumeTemplateService;

    @GetMapping(value = "findResumeByStudentNo/{studentNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllForPageable(@PathVariable("studentNo") String studentNo) {
        return ResponseUtil.success(studentResumeService.findByStudentNo(studentNo));
    }

    @PostMapping("/save")
    public ResponseEntity update(@RequestBody StudentResume studentResume) {
        studentResumeService.handleSave(studentResume);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/upload/{studentNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadPhoto(@PathVariable("studentNo") String studentNo, MultipartFile file ) {
        studentResumeService.saveResumePhoto(file, studentNo);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @GetMapping(value = "/export")
    public void exportResume(HttpServletResponse response, String studentNo) throws IOException {
        studentResumeService.exportResume(studentNo, response);
    }

    @GetMapping(value = "/findImageByStudentNo/{studentNo}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImageByStudentNo(@PathVariable("studentNo") String studentNo) throws IOException {
        return studentResumeService.getImageByStudentNo(studentNo);
    }

    @GetMapping(value = "/findAllActiveTemplate")
    public ResponseEntity getImageByStudentNo() {
        return ResponseUtil.success(resumeTemplateService.getAllActiveTemplateInfo());
    }

}
