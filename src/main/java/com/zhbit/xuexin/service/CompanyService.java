package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public void register(Company company) {
        if (null != company && !StringUtils.isEmpty(company.getSoleCode())) {
            // check
            Company existCompany = companyRepository.findBySoleCode(company.getSoleCode());
            if (null == existCompany)
                companyRepository.save(company);
            else
                throw new CustomException(ResultEnum.CompanySoleCodeDuplicateException.getMessage(), ResultEnum.CompanySoleCodeDuplicateException.getCode());
        } else
            throw new CustomException(ResultEnum.CompanyInfoException.getMessage(), ResultEnum.CompanyInfoException.getCode());
    }
}
