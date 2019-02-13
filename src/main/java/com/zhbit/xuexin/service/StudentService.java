package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public List<Student> save(Student student) {
        if (null != student) {
            // todo throwException
            if (StringUtils.isEmpty(student.getStuId())) {
                // add
                String stuNo = student.getStudentNo();
                Student isStudentExist = !StringUtils.isEmpty(stuNo) ? studentRepository.findByStudentNo(stuNo) : null;
                if (null == isStudentExist) {
                    studentRepository.save(student);
                } else {
                    throw new CustomException(ResultEnum.StudentNoDuplicatedException.getMessage(), ResultEnum.StudentNoDuplicatedException.getCode());
                }
            }
//            else {
//                // update
//            }
        }
        return findAll();
    }
}
