package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends IBaseRespository<Organization, String> {

    Organization findByOrgName(String orgName);

    @Query(value = "select * FROM tb_sys_organization where ACTIVE = 1 OR ACTIVE = 2", nativeQuery = true)
    List<Organization> findByActive();

    List<Organization> findByActive(Integer active);
}
