package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.service.ResumeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/xuexin/security/admin/resumeTemplate")
public class ResumeTemplateController {

    @Autowired
    ResumeTemplateService resumeTemplateService;

    @GetMapping(value = "/findAllActive", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageByStudentNo() throws IOException {
        return resumeTemplateService.getAllActiveTemplateInfo();
    }

}