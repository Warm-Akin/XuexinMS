package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.ExcelUtil;
import com.zhbit.xuexin.dto.CourseDto;
import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.repository.CourseRepository;
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
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public PageResultVO<Course> findAll(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Course> coursePage = (Page<Course>) courseRepository.findAll(new Specification<Course>() {

            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<Course> pageResultVO = new PageResultVO<>(coursePage.getContent(), coursePage.getTotalElements());
        return pageResultVO;
    }

    @Transactional
    public void handleSave(Course course) {
        if (null != course) {
            if (StringUtils.isEmpty(course.getCourseId())) {
                // add
                String courseCode = course.getCourseCode();
                Course isCourseExist = !StringUtils.isEmpty(courseCode) ? courseRepository.findByCourseCode(courseCode) : null;
                if (isCourseExist == null) {
                    course.setCreateTime(new Date());
                    course.setActive(Constant.ACTIVE);
                } else {
                    throw new CustomException(ResultEnum.CourseCodeDuplicatedException.getMessage(), ResultEnum.CourseCodeDuplicatedException.getCode());
                }

            } else {
                // update
                Course currentCourse = courseRepository.findById(course.getCourseId()).orElse(null);
                // set value for not allow modify Columns
                course.setCourseCode(currentCourse.getCourseCode());
            }
            courseRepository.save(course);
        }

    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseCode"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseName"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "academicYear"));
        return new Sort(orders);
    }

    public PageResultVO<Course> findByConditions(CourseDto courseDto) {
        Pageable pageable = PageRequest.of(courseDto.getCurrentPage() - 1, courseDto.getPageSize(), getSort());
        Page<Course> teacherPage = courseRepository.findAll(new Specification<Course>() {

            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(courseDto.getCourseCode())) {
                    Path courseCode = root.get("courseCode");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(courseCode), "%" + courseDto.getCourseCode().trim().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getCourseName())) {
                    Path courseName = root.get("courseName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(courseName), "%" + courseDto.getCourseName().trim().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getTeacherNo())) {
                    Path teacherNo = root.get("teacherNo");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherNo), "%" + courseDto.getTeacherNo().toUpperCase() + "%");
                    predicateList.add(p);
                }
                // todo upper
                if (!StringUtils.isEmpty(courseDto.getCourseName())) {
                    Path teacherName = root.get("teacherName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherName), "%" + courseDto.getCourseName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getAcademicYear())) {
                    Path academicYear = root.get("academicYear");
                    Predicate p = criteriaBuilder.equal(academicYear, courseDto.getAcademicYear());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getTerm())) {
                    Path term = root.get("term");
                    Predicate p = criteriaBuilder.equal(term, courseDto.getTerm());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getCourseType())) {
                    Path courseType = root.get("courseType");
                    Predicate p = criteriaBuilder.equal(courseType, courseDto.getCourseType().trim());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(courseDto.getCredit())) {
                    Path credit = root.get("credit");
                    Predicate p = criteriaBuilder.equal(credit, courseDto.getCredit());
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

        PageResultVO<Course> pageResultVO = new PageResultVO<>(teacherPage.getContent(), teacherPage.getTotalElements());
        return pageResultVO;
    }


    public void uploadCourseInfoList(MultipartFile file) {
        if (null != file) {
            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                // Ignore the title row
                Row row = rowIterator.next();
                List<Course> courseList = new ArrayList<>();
                int rowIndex=1;
                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    rowIndex += 1;
                    // teacher's necessary information can't be null
                    if (ExcelUtil.isFull(row, Constant.INDEX_COURSE_CODE, Constant.INDEX_COURSE_CREDIT)) {
                        Course course = new Course();
                        course.setCourseCode(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_CODE)));
                        course.setCourseName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_NAME)));
                        course.setAcademicYear(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_ACADEMICYEAR)));
                        course.setTerm(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_TERM)));
                        course.setTotalHours(Double.parseDouble(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_TOTAL_HOURS))));
                        course.setLabHours(Double.parseDouble(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_LAB_HOURS))));
                        course.setSelectedCourseNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_SELECTED_COURSE_NO)));

                        // todo check
                        Double limitStudentNum = Double.parseDouble(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_LIMIT_STUDENT_NUM)));
                        Double studentNum = Double.parseDouble(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_STUDENT_NUM)));
                        if (studentNum <= limitStudentNum) {
                            course.setLimitStudentNum(limitStudentNum);
                            course.setStudentNum(studentNum);
                        } else // todo 限选人数 > 选择人数
                            throw new CustomException(String.format(ResultEnum.CourseInfoError.getMessage(), String.valueOf(rowIndex)), ResultEnum.CourseInfoError.getCode());

                        course.setCourseType(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_COURSE_TYPE)));
                        course.setBelongTo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_BELONG_TO)));
                        course.setTeacherNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_TEACHER_NO)));
                        course.setTeacherName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_TEACHER_NAME)));
                        course.setClassInfo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_CLASSINFO)));
                        course.setMemo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_COURSE_MEMO)));
                        courseList.add(course);
                    } else
                        throw new CustomException(String.format(ResultEnum.CourseUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.CourseUploadIncomplete.getCode());
                }
                // do save in jdbcTemplate -> 去重
                saveCourseListForUpload(courseList);
            } else
                throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
        }
    }

    private void saveCourseListForUpload(List<Course> courseList) {
        List<Course> courseExistList = courseRepository.findAll();
        List<Course> duplicateCourseCodeList = new ArrayList<>();
        List<Course> newInsertCourseList = new ArrayList<>();
        courseList.forEach(course -> {
            Boolean isExist = compareCourseCode(course, courseExistList);
            if (isExist)
                duplicateCourseCodeList.add(course);
            else {
                // set ID and ACTIVE
                course.setCourseId(UUID.randomUUID().toString().replace("-", ""));
                course.setActive(Constant.ACTIVE);
                newInsertCourseList.add(course);
            }
        });
        // insert the new records
        if (!newInsertCourseList.isEmpty())
            insertCourses(newInsertCourseList);
        // todo duplicateCourseCodeList
    }

    private Boolean compareCourseCode(Course course, List<Course> courseExistList) {
        int listSize = courseExistList.size();
        Boolean isExist = false;
        for (int i = 0; i < listSize; i++) {
            if (course.getTeacherNo().trim().equals(courseExistList.get(i).getCourseCode())) {
                isExist = true;
                break;
            }
        }
        if (!isExist)
            courseExistList.add(course);
        return isExist;
    }

    private int insertCourses(List<Course> courseList) {
        String sql = "INSERT INTO t_courseinfo(ID, COURSECODE, COURSENAME, ACADEMICYEAR, TERM, TOTALHOURS, LABHOURS ,SELECTEDCOURSENO, LIMITSTUDENTNUM, STUDENTNUM, CREDIT, COURSETYPE," +
                " BELONGTO, EMPLOY_NO, EMPLOY_NAME, CLASSINFO, MEMO, ACTIVE) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Course course = courseList.get(i);
                preparedStatement.setString(1, course.getCourseId());
                preparedStatement.setString(2, course.getCourseCode());
                preparedStatement.setString(3, course.getCourseName());
                preparedStatement.setString(4, course.getAcademicYear());
                preparedStatement.setString(5, course.getTerm());
                preparedStatement.setDouble(6, course.getTotalHours());
                preparedStatement.setDouble(7, course.getLabHours());
                preparedStatement.setString(8, course.getSelectedCourseNo());
                preparedStatement.setDouble(9, course.getLimitStudentNum());
                preparedStatement.setDouble(10, course.getStudentNum());
                preparedStatement.setDouble(11, course.getCredit());
                preparedStatement.setString(12, course.getCourseType());
                preparedStatement.setString(13, course.getBelongTo());
                preparedStatement.setString(14, course.getTeacherNo());
                preparedStatement.setString(15, course.getTeacherName());
                preparedStatement.setString(16, course.getClassInfo());
                preparedStatement.setString(17, course.getMemo());
                preparedStatement.setInt(18, course.getActive());
            }

            @Override
            public int getBatchSize() {
                return courseList.size();
            }
        }).length;
    }
}
