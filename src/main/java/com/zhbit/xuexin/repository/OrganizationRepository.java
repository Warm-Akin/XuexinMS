package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends IBaseRespository<Organization, String> {
    Organization findByOrgName(String orgName);
}
