package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.Teacher;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.TeacherRepository;
import com.zhbit.xuexin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    StudentRepository studentRepository;
//
//    @Autowired
//    TeacherRepository teacherRepository;
//
//    public User checkLogin(User user) {
//        if (user != null && !StringUtils.isEmpty(user.getEmployName()) && !StringUtils.isEmpty(user.getPassword())) {
//            String userName = user.getEmployName();
//            String password = SecurityUtil.GetMD5Code(user.getPassword());
//            String userType = "";
//            // userType = 2
//            User currentUser = userRepository.findByEmployNameAndPassword(userName, password);
//            if (null != currentUser) {
//                userType = "2";
//            } else {
//                currentUser = new User();
//                currentUser.setEmployNo(userName);
//                // userType = 1
//                Student currentStudent = studentRepository.findByStudentNoAndPassword(userName, password);
//                if (null != currentStudent) {
//                    userType = "1";
//                } else {
//                    // userType = 0
//                    Teacher currentTeacher = teacherRepository.findByTeacherNoAndPassword(userName, password);
//                    if (null != currentTeacher) {
//                        userType = "0";
//                    }
//                }
//            }
//            if (!StringUtils.isEmpty(userType)) {
//                // 验证成功，获取权限信息
//                // todo
//                currentUser.setUserType(userType);
//            } else // 验证失败
//                throw new CustomException(ResultEnum.AccountInvalidException.getMessage(), ResultEnum.AccountInvalidException.getCode());
//            return currentUser;
//        }
//        return null;
//    }

    public User validateUserLogin(String userName, String password) throws UsernameNotFoundException {
        User currentUser = null;
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            currentUser = userRepository.findByEmployNameAndPassword(userName, SecurityUtil.GetMD5Code(password));
//            if (null != currentUser) {
//                // todo load user's authority
//                return currentUser;
//            } else
//                throw new CustomException(ResultEnum.AccountInvalidException.getMessage(), ResultEnum.AccountInvalidException.getCode());
        }
        return currentUser;
    }
}
