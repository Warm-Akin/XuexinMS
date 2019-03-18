package com.zhbit.xuexin.controller;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xuexin/anonymous")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping("/company/register")
    public ResponseEntity register(@RequestBody Company company) {
        companyService.register(company);
        return ResponseUtil.success(HttpStatus.OK);
    }

}
