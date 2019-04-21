package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends IBaseRespository<Course, String> {
    Course findByCourseCode(String courseCode);

    Course findBySelectedCourseNo(String selectedCourseNo);

    List<Course> findByActive(Integer active);
}