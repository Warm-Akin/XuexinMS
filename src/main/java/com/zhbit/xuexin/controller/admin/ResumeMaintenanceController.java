package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.constant.HttpCode;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.ResumeDto;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.service.PdfModelService;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/xuexin/security/admin/resume")
public class ResumeMaintenanceController {

    @Autowired
    StudentResumeService studentResumeService;

    @Autowired
    PdfModelService pdfModelService;

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findByConditions(@RequestBody ResumeDto resumeDto) {
        return ResponseUtil.success(studentResumeService.findByConditions(resumeDto));
    }

    @PostMapping(value = "/delete")
    public ResponseEntity deleteRecords(@RequestBody List<StudentResume> resumeList) {
        studentResumeService.deleteRecords(resumeList);
        return ResponseUtil.success(HttpCode.SUCCESS);
    }

    @GetMapping("/findAll/{page}/{pageSize}")
    public ResponseEntity findAll(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentResumeService.findAll(page, pageSize));
    }

    @PostMapping(value = "/pdfUpload")
    public ResponseEntity uploadStudentList(MultipartFile file) {
        pdfModelService.handlePdfFile(file);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @GetMapping(value = "/studentResumeExport")
    public void exportResume(HttpServletResponse response, String resumeUrl) throws IOException {
        studentResumeService.exportStudentResume(resumeUrl, response);
    }
}
