package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.dto.CompanyDto;
import com.zhbit.xuexin.dto.OrderDetailDto;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.model.Company;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.CompanyRepository;
import com.zhbit.xuexin.repository.OrganizationRepository;
import com.zhbit.xuexin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.Executor;

@Service
public class CompanyService {

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    Executor executor;

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
                // save company information to user
                asyncSavePasswordToUser(company);
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
            // save password information to user table
            asyncSavePasswordToUser(company);
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
                // todo (添加表或者字段记录企业缴费的日期和有效时长)
                String remainLimitation = currentCompany.getPdfLimit();
                String limitation = orderDetailDto.getType().equals("1") ? "+9999" : (!remainLimitation.equals("-1") ? String.valueOf(Integer.parseInt(remainLimitation) + Integer.parseInt(orderDetailDto.getTotalFee())) : orderDetailDto.getTotalFee());
                currentCompany.setPdfLimit(limitation);
                currentCompany.setUpdateBy(orderDetailDto.getCompanySoleCode());
                currentCompany.setUpdateDate(new Date().toLocaleString());
                companyRepository.save(currentCompany);
            }
        }
    }

    public Company findBySoleCode(String soleCode) {
        if (!StringUtils.isEmpty(soleCode)) {
            Company company = companyRepository.findBySoleCode(soleCode);
            return company;
        } else
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
    }

    @Transactional
    public void updateInfo(Company company) {
        if (null != company) {
            Company currentCompany = companyRepository.findBySoleCode(company.getSoleCode());
            if (currentCompany != null && company.getId().equals(currentCompany.getId())) {
                // the same object
                company.setCompanyName(currentCompany.getCompanyName());
                companyRepository.save(company);
            } else {
                logger.info("ID 或 SoleCode 被更改");
                throw new CustomException(ResultEnum.UpdateCompanyInfoException.getMessage(), ResultEnum.UpdateCompanyInfoException.getCode());
            }
        }
    }

    @Transactional
    public void updatePasswordInfo(PasswordDto passwordDto) {
        if (null != passwordDto && !StringUtils.isEmpty(passwordDto.getUserName())) {
            Company currentCompany = companyRepository.findBySoleCode(passwordDto.getUserName());
            if (null != currentCompany) {
                if (currentCompany.getPassword().equals(SecurityUtil.GetMD5Code(passwordDto.getOriginalPassword()))) {
                    currentCompany.setPassword(SecurityUtil.GetMD5Code(passwordDto.getNewPassword()));
                    // get the same information from User
                    User companyUser = userRepository.findByEmployNo(passwordDto.getUserName());
                    companyUser.setPassword(currentCompany.getPassword());
                    companyRepository.save(currentCompany);
                    userRepository.save(companyUser);
                } else
                    throw new CustomException(ResultEnum.OriginalPasswordErrorException.getMessage(), ResultEnum.OriginalPasswordErrorException.getCode());
            } else {
                logger.info("登录的公司SoleCode 被更改，用户不存在");
                throw new CustomException(ResultEnum.SoleCodeInfoException.getMessage(), ResultEnum.SoleCodeInfoException.getCode());
            }
        } else
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
    }

    private void asyncSavePasswordToUser(Company company) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    User user = userRepository.findByEmployNo(company.getSoleCode());
                    if (null == user) {
                        // insert
                        user = new User();
                        user.setEmployNo(company.getSoleCode());
                        user.setStatus(Double.parseDouble(Constant.USER_ENABLE));
                        user.setCreateTime(new Date());
                        // organization only for company -> active = 2;
                        Organization organization = organizationRepository.findByActive(2).get(0);
                        user.setOrganization(organization);
                        user.setUserType(Constant.USER_TYPE_COMPANY);
                    }
                    // common for insert or update
                    user.setPassword(company.getPassword());
                    user.setEmployName(company.getLegalName());
                    userRepository.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}