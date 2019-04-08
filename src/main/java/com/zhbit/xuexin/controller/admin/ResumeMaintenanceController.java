package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.constant.HttpCode;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.ResumeDto;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/xuexin/security/admin/resume")
public class ResumeMaintenanceController {

    @Autowired
    StudentResumeService studentResumeService;

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findByConditions(@RequestBody ResumeDto resumeDto) {
        return ResponseUtil.success(studentResumeService.findByConditions(resumeDto));
    }

    @PostMapping(value = "/delete")
    public ResponseEntity deleteRecords(@RequestBody List<StudentResume> resumeList) {
        studentResumeService.deleteRecords(resumeList);
        return ResponseUtil.success(HttpCode.SUCCESS);
    }
}
