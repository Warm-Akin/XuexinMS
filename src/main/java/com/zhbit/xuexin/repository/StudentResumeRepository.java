package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentResumeRepository extends IBaseRespository<StudentResume, String> {
    StudentResume findByStudentNo(String studentNo);
}
