package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.model.StudentCourseScoreDetail;
import com.zhbit.xuexin.repository.StudentCourseScoreDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCourseScoreDetailService {

    @Autowired
    StudentCourseScoreDetailRepository studentCourseScoreDetailRepository;

    public PageResultVO<StudentCourseScoreDetail> findAll(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<StudentCourseScoreDetail> resultPage = (Page<StudentCourseScoreDetail>) studentCourseScoreDetailRepository.findAll(new Specification<StudentCourseScoreDetail>() {

            @Override
            public Predicate toPredicate(Root<StudentCourseScoreDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<StudentCourseScoreDetail> pageResultVO = new PageResultVO<>(resultPage.getContent(), resultPage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseCode"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseName"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "academicYear"));
        return new Sort(orders);
    }
}
