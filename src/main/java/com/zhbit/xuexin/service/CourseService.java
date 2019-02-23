package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.repository.CourseRepository;
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
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public PageResultVO<Course> findAll(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Course> coursePage = (Page<Course>) courseRepository.findAll(new Specification<Course>() {

            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<Course> pageResultVO = new PageResultVO<>(coursePage.getContent(), coursePage.getTotalElements());
        return pageResultVO;
    }

    public void handleSave(Course course) {

    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseCode"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseName"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "academicYear"));
        return new Sort(orders);
    }

}
