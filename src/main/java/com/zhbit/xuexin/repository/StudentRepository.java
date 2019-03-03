package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.repository.common.IBaseRespository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends IBaseRespository<Student, String> {
    Student findByStudentNo(String stuNo);

    Student findByStudentNoAndPassword(String userName, String password);
}
