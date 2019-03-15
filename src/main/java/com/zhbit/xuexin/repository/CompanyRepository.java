package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends IBaseRespository<Company, String> {
    Company findBySoleCode(String soleCode);
}
