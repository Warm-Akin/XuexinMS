package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.dto.CompanyDto;
import com.zhbit.xuexin.dto.OrderDetailDto;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Transactional
    public void registerCompany(Company company) {
        if (null != company && !StringUtils.isEmpty(company.getSoleCode())) {
            // check
            Company existCompany = companyRepository.findBySoleCode(company.getSoleCode());
            if (null == existCompany) {
                String password = SecurityUtil.GetMD5Code(company.getPassword());
                company.setPassword(password);
                company.setDeleteFlag("0");
                company.setPdfLimit("-1");
                company.setActive(Constant.ACTIVE);
                companyRepository.save(company);
            } else
                throw new CustomException(ResultEnum.CompanySoleCodeDuplicateException.getMessage(), ResultEnum.CompanySoleCodeDuplicateException.getCode());
        } else
            throw new CustomException(ResultEnum.CompanyInfoException.getMessage(), ResultEnum.CompanyInfoException.getCode());
    }

    @Transactional
    public void saveCompany(Company company) {
        if (null != company && !StringUtils.isEmpty(company.getSoleCode())) {
            // check
            String password = company.getPassword();
            Company existCompany = companyRepository.findBySoleCode(company.getSoleCode());
            if (null == existCompany) {
                // insert
                company.setDeleteFlag("0");
                company.setPdfLimit("-1");
                company.setPassword(SecurityUtil.GetMD5Code(password));
                company.setActive(Constant.ACTIVE);
            } else {
                if (!company.getId().equals(existCompany.getId()))
                    throw new CustomException(ResultEnum.CompanySoleCodeDuplicateException.getMessage(), ResultEnum.CompanySoleCodeDuplicateException.getCode());
                else {
                    // update the same record
                    if (!password.equals(existCompany.getPassword()))
                        company.setPassword(SecurityUtil.GetMD5Code(password));
                }
            }
            companyRepository.save(company);
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

    public PageResultVO<Company> findByConditions(CompanyDto companyDto) {
        Pageable pageable = PageRequest.of(companyDto.getCurrentPage() - 1, companyDto.getPageSize(), getSort());
        Page<Company> resultPage = companyRepository.findAll(new Specification<Company>() {

            @Override
            public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(companyDto.getSoleCode())) {
                    Path soleCode = root.get("soleCode");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(soleCode), "%" + companyDto.getSoleCode().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(companyDto.getCompanyName())) {
                    Path companyName = root.get("companyName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(companyName), "%" + companyDto.getCompanyName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(companyDto.getLegalName())) {
                    Path legalName = root.get("legalName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(legalName), "%" + companyDto.getLegalName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                // active = 1
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageable);

        PageResultVO<Company> pageResultVO = new PageResultVO<>(resultPage.getContent(), resultPage.getTotalElements());
        return pageResultVO;
    }

    @Transactional
    public void delete(List<Company> companyList) {
        if (companyList.size() > 0) {
            companyList.forEach(company -> {
                company.setActive(Constant.INACTIVE);
            });
            companyRepository.saveAll(companyList);
        } else
            throw new CustomException(ResultEnum.CompanyDeleteFailedException.getMessage(), ResultEnum.CompanyDeleteFailedException.getCode());
    }

    @Transactional
    public void updateAfterPayment(OrderDetailDto orderDetailDto) {
        if (!StringUtils.isEmpty(orderDetailDto.getCompanySoleCode())) {
            Company currentCompany = companyRepository.findBySoleCode(orderDetailDto.getCompanySoleCode());
            if (null != currentCompany) {
                String limitation = orderDetailDto.getType().equals("1") ? "+9999" : orderDetailDto.getTotalFee();
                currentCompany.setPdfLimit(limitation);
                currentCompany.setUpdateBy(orderDetailDto.getCompanySoleCode());
                currentCompany.setUpdateDate(new Date().toLocaleString());
                companyRepository.save(currentCompany);
            }
        }
    }
}
