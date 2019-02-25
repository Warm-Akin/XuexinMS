package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.repository.common.IBaseRespository;

public interface CourseRepository extends IBaseRespository<Course, String> {
    Course findByCourseCode(String courseCode);
}