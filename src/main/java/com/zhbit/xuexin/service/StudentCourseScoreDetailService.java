package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentCourseScoreDetail;
import com.zhbit.xuexin.repository.CourseRepository;
import com.zhbit.xuexin.repository.OrganizationRepository;
import com.zhbit.xuexin.repository.StudentCourseScoreDetailRepository;
import com.zhbit.xuexin.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentCourseScoreDetailService {

    @Autowired
    StudentCourseScoreDetailRepository studentCourseScoreDetailRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CourseRepository courseRepository;

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

    public void save(StudentCourseScoreDetail studentCourseScoreDetail) {
        if (null != studentCourseScoreDetail) {
            Student student = studentRepository.findByStudentNo(studentCourseScoreDetail.getStudentNo());
            Organization organization = organizationRepository.findByOrgName(studentCourseScoreDetail.getOrgName());
            Course course = courseRepository.findBySelectedCourseNo(studentCourseScoreDetail.getSelectedCourseNo());
            // The same operation  when a save or modification is performed
            // To ensure data consistency, the same attributes need to be copied from other existing entities to prevent modification
            BeanUtils.copyProperties(student, studentCourseScoreDetail);
//            BeanUtils.copyProperties();
            studentCourseScoreDetail.setOrgId(organization.getOrgId());
            BeanUtils.copyProperties(course, studentCourseScoreDetail, "orgId", "orgName", "major", "majorCode");
            if (StringUtils.isEmpty(studentCourseScoreDetail.getId())) {
                // insert
                // todo add a listener for create time and creator
                studentCourseScoreDetail.setCreateTime(new Date());
            }
            studentCourseScoreDetail.setActive(Constant.ACTIVE);

            studentCourseScoreDetailRepository.save(studentCourseScoreDetail);
        } else
            throw new CustomException(ResultEnum.EntityIsNullException.getMessage(), ResultEnum.EntityIsNullException.getCode());
    }
}
