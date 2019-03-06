package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentResume;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.StudentResumeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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

    @Transactional
    public void handleSave(StudentResume studentResume) {
        resumeRepository.save(studentResume);
    }

    @Transactional
    public void saveResumePhoto(MultipartFile file, String studentNo) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            StudentResume studentResume = resumeRepository.findByStudentNo(studentNo);
            String targetFileName = studentNo + suffixName;
            String targetPath = Constant.PHOTO_DIRECTORY_PATH + targetFileName;
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(targetFile);
                studentResume.setPhotoPath(targetPath);
                resumeRepository.save(studentResume);
//                System.out.println("success");
            } catch (IOException e) {
                throw new CustomException(ResultEnum.SaveResumePhotoException.getMessage(), ResultEnum.SaveResumePhotoException.getCode());
            }
        } else
            throw new CustomException(ResultEnum.ResumePhotoIsNullException.getMessage(), ResultEnum.ResumePhotoIsNullException.getCode());
    }
}
