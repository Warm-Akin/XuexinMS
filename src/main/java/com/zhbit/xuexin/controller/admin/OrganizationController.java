package com.zhbit.xuexin.controller.admin;

import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.OrganizationDto;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/xuexin/security/admin/organization")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping(value = "/findAllActive")
    public ResponseEntity findAllActive () {
        return ResponseUtil.success(organizationService.findAllActive());
    }

    @GetMapping(value = "/findActivePage/{page}/{pageSize}")
    public ResponseEntity findAllForPageable(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize) {
        return ResponseUtil.success(organizationService.findAllForPageable(page, pageSize));
    }

    @PostMapping(value = "/findByConditions")
    public ResponseEntity findByConditions (@RequestBody OrganizationDto organizationDto) {
        return ResponseUtil.success(organizationService.findByConditions(organizationDto));
    }

    @PostMapping(value = "/save")
    public ResponseEntity saveOrganization (@RequestBody Organization organization) {
        organizationService.saveOrganization(organization);
        return ResponseUtil.success(HttpStatus.OK);
    }

    @PostMapping(value = "/remove")
    public ResponseEntity removeOrganizations (@RequestBody List<Organization> organizationList) {
        organizationService.removeOrganizations(organizationList);
        return ResponseUtil.success(HttpStatus.OK);
    }

}