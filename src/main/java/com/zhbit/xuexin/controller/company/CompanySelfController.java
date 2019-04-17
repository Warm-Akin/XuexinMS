package com.zhbit.xuexin.controller.company;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.OrderDetailDto;
import com.zhbit.xuexin.service.CompanyService;
import com.zhbit.xuexin.service.StudentResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/xuexin/security/company")
public class CompanySelfController {

    @Autowired
    CompanyService companyService;

    @PostMapping("/updateLimitation")
    public ResponseEntity findAll(@RequestBody OrderDetailDto orderDetailDto) {
        companyService.updateAfterPayment(orderDetailDto);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @GetMapping("/findBySoleCode/{soleCode}")
    public ResponseEntity findBySoleCode(@PathVariable("soleCode") String soleCode) {
        return ResponseUtil.success(companyService.findBySoleCode(soleCode));
    }
}
