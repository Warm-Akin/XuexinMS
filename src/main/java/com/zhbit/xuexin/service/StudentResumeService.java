package com.zhbit.xuexin.service;

import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.StudentResumeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StudentResumeService {

    @Autowired
    StudentResumeRepository resumeRepository;

    @Autowired
    StudentRepository studentRepository;

    public StudentResume findByStudentNo(String studentNo) {

        StudentResume studentResume = null;
        if (!StringUtils.isEmpty(studentNo)) {
            studentResume = resumeRepository.findByStudentNo(studentNo);
            if (null == studentResume) {
                studentResume = new StudentResume();
                Student student = studentRepository.findByStudentNo(studentNo);
                // copy the same properties from Student
                BeanUtils.copyProperties(student, studentResume);
            }
        }
        return studentResume;
    }
}
