package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public void register(Company company) {
        if (null != company && !StringUtils.isEmpty(company.getSoleCode())) {
            // check
            Company existCompany = companyRepository.findBySoleCode(company.getSoleCode());
            if (null == existCompany) {
                String password = SecurityUtil.GetMD5Code(company.getPassword());
                company.setPassword(password);
                companyRepository.save(company);
            } else
                throw new CustomException(ResultEnum.CompanySoleCodeDuplicateException.getMessage(), ResultEnum.CompanySoleCodeDuplicateException.getCode());
        } else
            throw new CustomException(ResultEnum.CompanyInfoException.getMessage(), ResultEnum.CompanyInfoException.getCode());
    }

    public PageResultVO<Company> findActiveList(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Company> resultPage = (Page<Company>) companyRepository.findAll(new Specification<Company>() {

            @Override
            public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageRequest);
        PageResultVO<Company> pageResultVO = new PageResultVO<>(resultPage.getContent(), resultPage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "soleCode"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "companyName"));
        return new Sort(orders);
    }
}
