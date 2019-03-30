package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.constant.HttpCode;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.CompanyDto;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/xuexin/admin/company")
public class CompanyMaintenanceController {

    @Autowired
    CompanyService companyService;

    @GetMapping(value = "/findActiveList/{page}/{pageSize}")
    public ResponseEntity findActiveList(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(companyService.findActiveList(page, pageSize));
    }

    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody Company company) {
        companyService.saveCompany(company);
        return ResponseUtil.success(HttpCode.SUCCESS);
    }

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findByConditions(@RequestBody CompanyDto companyDto) {
        return ResponseUtil.success(companyService.findByConditions(companyDto));
    }

    @PostMapping(value = "/deleteRecords")
    public ResponseEntity deleteRecords(@RequestBody List<Company> companyList) {
        companyService.delete(companyList);
        return ResponseUtil.success(HttpCode.SUCCESS);
    }
}
