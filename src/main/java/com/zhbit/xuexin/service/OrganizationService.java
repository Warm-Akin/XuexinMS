package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Organization> findAllActive() {
        return organizationRepository.findByActive(Constant.ACTIVE);
    }
}
