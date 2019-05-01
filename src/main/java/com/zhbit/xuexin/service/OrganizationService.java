package com.zhbit.xuexin.service;

import com.alipay.api.domain.CustomerEntity;
import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.dto.OrganizationDto;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.repository.OrganizationRepository;
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
import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Organization> findAllActive() {
        return organizationRepository.findByActive();
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

    public PageResultVO<Organization> findAllForPageable(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Organization> organizationPage = (Page<Organization>) organizationRepository.findAll(new Specification<Organization>() {

            @Override
            public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<Organization> pageResultVO = new PageResultVO<>(organizationPage.getContent(), organizationPage.getTotalElements());
        return pageResultVO;
    }

    public PageResultVO<Organization> findByConditions(OrganizationDto organizationDto) {
        Pageable pageable = PageRequest.of(organizationDto.getCurrentPage() - 1, organizationDto.getPageSize(), getSort());
        Page<Organization> organizationPage = organizationRepository.findAll(new Specification<Organization>() {

            @Override
            public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(organizationDto.getOrgName())) {
                    Path orgName = root.get("orgName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(orgName), "%" + organizationDto.getOrgName().toUpperCase().trim() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(organizationDto.getContactMan())) {
                    Path contactMan = root.get("contactMan");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(contactMan), "%" + organizationDto.getContactMan().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(organizationDto.getAddress())) {
                    Path address = root.get("address");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(address), "%" + organizationDto.getAddress().toUpperCase() + "%");
                    predicateList.add(p);
                }

                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageable);

        PageResultVO<Organization> pageResultVO = new PageResultVO<>(organizationPage.getContent(), organizationPage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orgName"));
        return new Sort(orders);
    }

    @Transactional
    public void saveOrganization(Organization organization) {
        if (null != organization && !StringUtils.isEmpty(organization.getOrgName())) {
            Organization currentOrganization = organizationRepository.findByOrgName(organization.getOrgName());
            if (null == organization.getOrgId() && null == currentOrganization) {
                // insert
                organization.setActive(Constant.ACTIVE);
            } else if (!StringUtils.isEmpty(organization.getOrgId()) && null != currentOrganization && organization.getOrgId().equals(currentOrganization.getOrgId())) {
                // update
                // unchangeable properties
                organization.setOrgName(currentOrganization.getOrgName());
            } else // exception
                throw new CustomException(ResultEnum.OrganizationSaveException.getMessage(), ResultEnum.OrganizationSaveException.getCode());
            organizationRepository.save(organization);
        } else
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
    }

    @Transactional
    public void removeOrganizations(List<Organization> organizationList) {
        if (!organizationList.isEmpty()) {
            organizationList.forEach(organization -> organization.setActive(Constant.INACTIVE));
            organizationRepository.saveAll(organizationList);
        } else
            throw new CustomException(ResultEnum.DeleteFailedException.getMessage(), ResultEnum.DeleteFailedException.getCode());
    }
}
