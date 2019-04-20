package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Organization> findAllActive() {
        return organizationRepository.findByActive(Constant.ACTIVE);
    }

    // check orgName's validation and return orgId
    public String checkOrgNameReturnOrgId(String organizationName, List<Organization> organizationList) {
        if (!StringUtils.isEmpty(organizationName)) {
            int size = organizationList.size();
            for (int i = 0; i < size; i++) {
                if (organizationList.get(i).getOrgName().equals(organizationName))
                    return organizationList.get(i).getOrgId();
            }
        }
        return null;
    }
}
