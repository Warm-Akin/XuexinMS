package com.zhbit.xuexin.repository;

import com.zhbit.xuexin.model.Teacher;
import com.zhbit.xuexin.repository.common.IBaseRespository;

public interface TeacherRepository extends IBaseRespository<Teacher, String> {
    Teacher findByTeacherNo(String teacherNo);

    Teacher findByTeacherNoAndPassword(String userName, String password);
}
