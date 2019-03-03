package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.util.DateUtil;
import com.zhbit.xuexin.common.util.ExcelUtil;
import com.zhbit.xuexin.dto.StudentDto;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.repository.StudentRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PageResultVO<Student> findAll(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Student> studentPage = (Page<Student>) studentRepository.findAll(new Specification<Student>() {

            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageRequest);
        PageResultVO<Student> pageResultVO = new PageResultVO<>(studentPage.getContent(), studentPage.getTotalElements());
        return pageResultVO;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public PageResultVO<Student> findByConditions(StudentDto studentDto) {
        // todo check conditions
        Pageable pageable = PageRequest.of(studentDto.getCurrentPage() - 1, studentDto.getPageSize(), getSort());
//        PageRequest pageRequest = PageRequest.of(studentDto.getCurrentPage() - 1, studentDto.getPageSize(), getSort());
//        Page<Student> studentPage = studentRepository.findAll(pageRequest);
        Page<Student> studentPage = studentRepository.findAll(new Specification<Student>() {

            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(studentDto.getStudentNo())) {
                    Path studentNo = root.get("studentNo");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(studentNo), "%" + studentDto.getStudentNo().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getStudentName())) {
                    Path studentName = root.get("studentName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(studentName), "%" + studentDto.getStudentName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getSex())) {
                    Path sex = root.get("sex");
                    Predicate p = criteriaBuilder.equal(sex, studentDto.getSex());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getPoliticalStatus())) {
                    Path politicalStatus = root.get("politicalStatus");
                    Predicate p = criteriaBuilder.equal(politicalStatus, studentDto.getPoliticalStatus());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getAcceptanceYear())) {
                    Path acceptanceDate = root.get("acceptanceDate");
                    Predicate p = criteriaBuilder.like(acceptanceDate.as(String.class), "%" + studentDto.getAcceptanceYear() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getOrgName())) {
                    Path orgName = root.get("orgName");
                    Predicate p = criteriaBuilder.equal(orgName, studentDto.getOrgName());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getMajor())) {
                    Path major = root.get("major");
                    Predicate p = criteriaBuilder.equal(major, studentDto.getMajor());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentDto.getClassName())) {
                    Path className = root.get("className");
                    Predicate p = criteriaBuilder.equal(className, studentDto.getClassName());
                    predicateList.add(p);
                }
                // active = 1
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, Constant.ACTIVE);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        }, pageable);

        PageResultVO<Student> pageResultVO = new PageResultVO<>(studentPage.getContent(), studentPage.getTotalElements());
        return pageResultVO;
    }

    @Transactional
    public void handleSave(Student student) {
        if (null != student) {
            if (StringUtils.isEmpty(student.getStuId())) {
                // add
                String stuNo = student.getStudentNo();
                Student isStudentExist = !StringUtils.isEmpty(stuNo) ? studentRepository.findByStudentNo(stuNo) : null;
                if (null == isStudentExist) {
                    // todo set createdTime and creator automatically
                    student.setCreateTime(new Date());
                    studentRepository.save(student);
                } else {
                    throw new CustomException(ResultEnum.StudentNoDuplicatedException.getMessage(), ResultEnum.StudentNoDuplicatedException.getCode());
                }
            } else {
                // update
                // not allow to modify some Columns
                Student currentStudent = studentRepository.findById(student.getStuId()).orElse(null);
                student.setStudentNo(currentStudent.getStudentNo());
                student.setAcceptanceDate(currentStudent.getAcceptanceDate());
                student.setClassName(currentStudent.getClassName());
                student.setCultivateDirection(currentStudent.getCultivateDirection());
                student.setDepartment(currentStudent.getDepartment());
                student.setOrgName(currentStudent.getOrgName());
                student.setMajor(currentStudent.getMajor());
                student.setMajorCategories(currentStudent.getMajorCategories());
                student.setEducationSystem(currentStudent.getEducationSystem());
            }
            student.setBirthday(DateUtil.formatDate(student.getIdcardNo().substring(6, 13)));
            studentRepository.save(student);
        }
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "studentNo"));
        return new Sort(orders);
    }

    public void uploadStudentList(MultipartFile file) {
        if (null != file) {
            // todo import
            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                // Ignore the title row
                Row row = rowIterator.next();
                List<Student> studentList = new ArrayList<>();
                int rowIndex = 1;
                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    rowIndex += 1;
                    // student's necessary information can't be null
                    if (ExcelUtil.isFull(row, Constant.INDEX_STUDENT_NO, Constant.INDEX_CLASSNAME)) {
                        Student student = new Student();
                        student.setStudentNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_NO)));
                        student.setStudentName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_NAME)));
                        student.setSex(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_SEX)));
                        student.setIdcardNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ID_CARD_NO)));
                        student.setOrgName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ORGANIZATION_NAME)));
                        // todo 学院(判断是否存在)
                        student.setMajor(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MAJOR_NAME)));
                        student.setMajorCategories(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MAJOR_CATEGORY)));
                        student.setClassName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_CLASSNAME)));
                        // set unnecessary columns
                        student.setPoliticalStatus(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_POLITICAL_STATUS)));
                        student.setNation(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_NATION)));
                        student.setNativePlace(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_NATIVE_PLACE)));
                        student.setFromPlace(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_FROM_PLACE)));
                        student.setEducationSystem(Integer.parseInt(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_EDUCATION_SYSTEM))));
                        student.setSchoolingLength(Integer.parseInt(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_SCHOOLING_LENGTH))));
                        student.setCultivateDirection(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_CULTIVATE_DIRECTION)));
                        student.setAcceptanceDate(DateUtil.formatDate(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ACCEPTANCE_DATE))));
                        student.setMiddleSchool(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MIDDLE_SCHOOL)));
                        student.setEmail(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_EMAIL)));
                        student.setMobileNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MOBILE_NO)));
                        student.setFamilyTelNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_FAMILY_TEL_NO)));
                        student.setPostcode(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_POST_CODE)));
                        student.setTravelRange(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TRAVEL_RANGE)));
                        student.setAddress(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ADDRESS)));
                        student.setSkill(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_SKILL)));
                        // Computed attribute -> Birthday, Grade
                        student.setBirthday(DateUtil.formatDate(student.getIdcardNo().substring(6, 13)));
                        student.setGrade(Constant.GRADE_PREFIX + student.getStudentNo().substring(0, 2));
                        studentList.add(student);
                    } else
                        throw new CustomException(String.format(ResultEnum.StudentUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.StudentUploadIncomplete.getCode());
                }
                // do save in jdbcTemplate -> 去重
                // todo
                saveStudentListForUpload(studentList);
            }
        } else
            throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
    }

    private void saveStudentListForUpload(List<Student> studentList) {
        List<Student> studentExistList = studentRepository.findAll();
        List<Student> duplicateStudentNoList = new ArrayList<>();
        List<Student> newInsertStudentList = new ArrayList<>();
        studentList.forEach(student -> {
            Boolean isExist = compareStudentNo(student, studentExistList);
            if (isExist)
                duplicateStudentNoList.add(student);
            else {
                // set ID and ACTIVE
                student.setStuId(UUID.randomUUID().toString().replace("-", ""));
                student.setActive(Constant.ACTIVE);
                newInsertStudentList.add(student);
            }
        });
        // insert the new records
        if (!newInsertStudentList.isEmpty())
            insertStudents(newInsertStudentList); // todo 学院id password
        // todo duplicateStudentList
    }

    private Boolean compareStudentNo(Student student, List<Student> studentExistList) {
        int listSize = studentExistList.size();
        Boolean isExist = false;
        for (int i = 0; i < listSize; i++) {
            if (student.getStudentNo().trim().equals(studentExistList.get(i).getStudentNo())) {
                isExist = true;
                break;
            }
        }
        // 当前学号不存在，则加入现有的学生List
        if (!isExist)
            studentExistList.add(student);
        return isExist;
    }

    private int insertStudents(List<Student> students) {
        // todo 学院id
        String sql = "INSERT INTO t_students(STU_ID, STUDENTNO, STUNAME, SEX, IDCARDNO, ORG_NAME, MAJOR ,MAJORCATEGORIES, CLASSNAME, POLITICALSTATUS, NATION, NATIVEPLACE," +
                " FROM_PLACE, EDUCATIONSYSTEM, SCHOOLINGLENGTH, CULTIVATEDIRECTION, ACCEPTANCEDATE, MIDDLESCHOOL, EMAIL, MOBILENO, FAMILYTELNO, POSTCODE, TRAVELRANGE, ADDRESS, SKILL, GRADE, ACTIVE, BIRTHDAY) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Student student = students.get(i);
                preparedStatement.setString(1, student.getStuId());
                preparedStatement.setString(2, student.getStudentNo());
                preparedStatement.setString(3, student.getStudentName());
                preparedStatement.setString(4, student.getSex());
                preparedStatement.setString(5, student.getIdcardNo());
                preparedStatement.setString(6, student.getOrgName());
                preparedStatement.setString(7, student.getMajor());
                preparedStatement.setString(8, student.getMajorCategories());
                preparedStatement.setString(9, student.getClassName());
                preparedStatement.setString(10, student.getPoliticalStatus());
                preparedStatement.setString(11, student.getNation());
                preparedStatement.setString(12, student.getNativePlace());
                preparedStatement.setString(13, student.getFromPlace());
                preparedStatement.setInt(14, student.getEducationSystem());
                preparedStatement.setInt(15, student.getSchoolingLength());
                preparedStatement.setString(16, student.getCultivateDirection());
                preparedStatement.setDate(17, new java.sql.Date(student.getAcceptanceDate().getTime()));
                preparedStatement.setString(18, student.getMiddleSchool());
                preparedStatement.setString(19, student.getEmail());
                preparedStatement.setString(20, student.getMobileNo());
                preparedStatement.setString(21, student.getFamilyTelNo());
                preparedStatement.setString(22, student.getPostcode());
                preparedStatement.setString(23, student.getTravelRange());
                preparedStatement.setString(24, student.getAddress());
                preparedStatement.setString(25, student.getSkill());
                preparedStatement.setString(26, student.getGrade());
                preparedStatement.setInt(27, student.getActive());
                preparedStatement.setDate(28, new java.sql.Date(student.getBirthday().getTime()));
            }

            @Override
            public int getBatchSize() {
                return students.size();
            }
        }).length;
    }

    public Student findByStudentNo(String studentNo) {
        Student student = null;
        if (!StringUtils.isEmpty(studentNo))
            student = studentRepository.findByStudentNo(studentNo);
        return student;
    }
}