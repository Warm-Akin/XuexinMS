package com.zhbit.xuexin.controller.company;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/xuexin/security/company/resume")
public class CompanyResumeController {

    @Autowired
    StudentResumeService studentResumeService;

    @GetMapping("/findAll/{page}/{pageSize}")
    public ResponseEntity findAll(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(studentResumeService.findAll(page, pageSize));
    }

    @GetMapping(value = "/studentResumeExport")
    public void exportResume(HttpServletResponse response, String resumeUrl, String soleCode) throws IOException {
        studentResumeService.exportStudentResumeForCompany(soleCode, resumeUrl, response);
    }
}
