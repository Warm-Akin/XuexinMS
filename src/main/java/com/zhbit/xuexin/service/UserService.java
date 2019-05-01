package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.dto.PasswordDto;
import com.zhbit.xuexin.model.*;
import com.zhbit.xuexin.repository.CompanyRepository;
import com.zhbit.xuexin.repository.OrganizationRepository;
import com.zhbit.xuexin.repository.StudentRepository;
import com.zhbit.xuexin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

//    @Autowired
//    StudentRepository studentRepository;
//
//    @Autowired
//    CompanyRepository companyRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

//    @Autowired
//    Executor executor;

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

    public PageResultVO<User> findAllActiveUsers(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<User> userPage = (Page<User>) userRepository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Path status = root.get("status");
                Predicate p = criteriaBuilder.equal(status, Double.parseDouble(Constant.USER_ENABLE));
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageRequest);
        PageResultVO<User> pageResultVO = new PageResultVO<>(userPage.getContent(), userPage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "employNo"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "employName"));
        return new Sort(orders);
    }

    @Transactional
    public void saveUser(User user, String organizationName) {
        if (null != user) {
            User currentUser = userRepository.findByEmployNo(user.getEmployNo());
            String password = user.getPassword();
            if (user.getUserId() == null) {
                user.setPassword(SecurityUtil.GetMD5Code(password));
                if (currentUser != null)
                    throw new CustomException(ResultEnum.UserNameDuplicateException.getMessage(), ResultEnum.UserNameDuplicateException.getCode());
            } else {
                // userId != null
                if (currentUser == null || (null != currentUser && !user.getUserId().equals(currentUser.getUserId()))) {
                    logger.error("用户EmployNo被修改");
                    throw new CustomException(ResultEnum.UserNameNotExistException.getMessage(), ResultEnum.UserNameNotExistException.getCode());
                }
                if (!password.equals(currentUser.getPassword()))
                    user.setPassword(SecurityUtil.GetMD5Code(password));
            }
            Organization organization = organizationRepository.findByOrgName(organizationName);
            user.setOrganization(organization);
            userRepository.save(user);
//            if (!user.getUserType().equals(Constant.USER_TYPE_ADMIN))
//                asyncSaveUserToOtherEntity(user);
        } else
            throw new CustomException(ResultEnum.ParamsIsNullException.getMessage(), ResultEnum.ParamsIsNullException.getCode());
    }

//    private void asyncSaveUserToOtherEntity(User user) {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    insertUserToOtherEntity(user);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Transactional
//    protected void insertUserToOtherEntity(User user) {
//        String mobileNo = user.getTelephone() != null ? user.getTelephone() : "";
//        if (user.getUserType().equals(Constant.USER_TYPE_STUDENT)) {
//            // student
//            Student student = new Student();
//            student.setStudentNo(user.getEmployNo());
//            student.setStudentName(user.getEmployName());
//            student.setPassword(user.getPassword());
//            student.setActive(Constant.ACTIVE);
//            student.setOrgId(user.getOrganization().getOrgId());
//            student.setMobileNo(mobileNo);
//            studentRepository.save(student);
//        } else {
//            // company
//            Company company = new Company();
//            company.setSoleCode(user.getEmployNo());
//            company.setCreateDate(new Date().toLocaleString());
//            company.setLegalName(user.getEmployName());
//            company.setPdfLimit("-1");
//            company.setUpdateDate(new Date().toLocaleString());
//            company.setCompanyPhone(mobileNo);
//            company.setPassword(user.getPassword());
//            company.setActive(Constant.ACTIVE);
//            companyRepository.save(company);
//        }
//    }

}