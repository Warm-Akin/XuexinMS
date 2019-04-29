package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.model.User;
import com.zhbit.xuexin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String getUserTypeByUserName(String username) {
        User user = userRepository.findByEmployNo(username);
        String userType = "";
        switch (user.getUserType()) {
            case "0":
                userType = "ROLE_TEACHER";
                break;
            case "1":
                userType = "ROLE_STUDENT";
                break;
            case "2":
                userType = "ROLE_ADMIN";
                break;
            case "3":
                userType = "ROLE_COMPANY";
                break;
            default:
                userType = "ROLE_NONE";
        }
        return userType;
    }

    @Transactional
    public void updatePassword(PasswordDto passwordDto) {
        if (null != passwordDto && !StringUtils.isEmpty(passwordDto.getUserName())) {
            User currentUser = userRepository.findByEmployNoAndPassword(passwordDto.getUserName(), SecurityUtil.GetMD5Code(passwordDto.getOriginalPassword()));
            if (null != currentUser) {
                currentUser.setPassword(SecurityUtil.GetMD5Code(passwordDto.getNewPassword()));
                userRepository.save(currentUser);
            } else
                throw new CustomException(ResultEnum.OriginalPasswordErrorException.getMessage(), ResultEnum.OriginalPasswordErrorException.getCode());
        } else {
            logger.info("提交数据为空");
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
        }
    }

    @Transactional
    public int insertUserList(List<User> userList) {
        String sql = "INSERT INTO tb_sys_user(USER_ID, EMPLOY_NO, EMPLOY_NAME, SEX, ORG_ID, CREATE_TIME, TELL, STATUS, EMAIL, PASSWORD, USER_TYPE) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                User user = userList.get(i);
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getEmployNo());
                preparedStatement.setString(3, user.getEmployName());
                preparedStatement.setString(4, user.getSex());
                preparedStatement.setString(5, user.getOrganization().getOrgId());
                preparedStatement.setDate(6, new java.sql.Date(new Date().getTime()));
                preparedStatement.setString(7, user.getTelephone());
                preparedStatement.setDouble(8, user.getStatus());
                preparedStatement.setString(9, user.getEmail());
                preparedStatement.setString(10, user.getPassword());
                preparedStatement.setString(11, user.getUserType());
            }

            @Override
            public int getBatchSize() {
                return userList.size();
            }
        }).length;
    }

    @Transactional
    public void removeUsersFromStudentList(List<String> employNoList) {
        if (!employNoList.isEmpty()) {
            List<User> userList = userRepository.findByEmployNoList(employNoList);
            if (!userList.isEmpty()) {
                userList.forEach(user -> user.setStatus(Double.parseDouble(Constant.USER_DISABLE)));
                userRepository.saveAll(userList);
            }
        }
    }

    public User findByEmployNo(String username) {
        return userRepository.findByEmployNo(username);
    }
}