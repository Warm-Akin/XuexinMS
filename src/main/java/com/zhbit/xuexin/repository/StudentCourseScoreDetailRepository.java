package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.StudentCourseScoreDetail;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseScoreDetailRepository extends IBaseRespository<StudentCourseScoreDetail, String> {

    List<StudentCourseScoreDetail> findByActive(Integer active);
}
