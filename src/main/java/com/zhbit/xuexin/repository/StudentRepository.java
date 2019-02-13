package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends IBaseRespository<Student,String> {
    Student findByStudentNo(String stuNo);
}
