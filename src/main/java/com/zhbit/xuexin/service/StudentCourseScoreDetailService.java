package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.ExcelUtil;
import com.zhbit.xuexin.dto.StudentCourseScoreDetailDto;
import com.zhbit.xuexin.model.Course;
import com.zhbit.xuexin.model.Organization;
import com.zhbit.xuexin.model.Student;
import com.zhbit.xuexin.model.StudentCourseScoreDetail;
import com.zhbit.xuexin.repository.CourseRepository;
import com.zhbit.xuexin.repository.OrganizationRepository;
import com.zhbit.xuexin.repository.StudentCourseScoreDetailRepository;
import com.zhbit.xuexin.repository.StudentRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.Executor;

@Service
public class StudentCourseScoreDetailService {

    @Autowired
    StudentCourseScoreDetailRepository studentCourseScoreDetailRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    Executor executor;

    public PageResultVO<StudentCourseScoreDetail> findAll(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<StudentCourseScoreDetail> resultPage = (Page<StudentCourseScoreDetail>) studentCourseScoreDetailRepository.findAll(new Specification<StudentCourseScoreDetail>() {

            @Override
            public Predicate toPredicate(Root<StudentCourseScoreDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<StudentCourseScoreDetail> pageResultVO = new PageResultVO<>(resultPage.getContent(), resultPage.getTotalElements());
        return pageResultVO;
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseCode"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "courseName"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "academicYear"));
        return new Sort(orders);
    }

    public void save(StudentCourseScoreDetail studentCourseScoreDetail) {
        if (null != studentCourseScoreDetail) {
            Student student = studentRepository.findByStudentNo(studentCourseScoreDetail.getStudentNo());
            Organization organization = organizationRepository.findByOrgName(studentCourseScoreDetail.getOrgName());
            Course course = courseRepository.findBySelectedCourseNo(studentCourseScoreDetail.getSelectedCourseNo());
            // The same operation  for update or save
            // To ensure data consistency, the same attributes need to be copied from other existing entities to prevent modification
            BeanUtils.copyProperties(student, studentCourseScoreDetail);
            studentCourseScoreDetail.setOrgId(organization.getOrgId());
            BeanUtils.copyProperties(course, studentCourseScoreDetail, "orgId", "orgName", "major", "majorCode");
            if (StringUtils.isEmpty(studentCourseScoreDetail.getId())) {
                // insert
                studentCourseScoreDetail.setCreateTime(new Date());
            }
            studentCourseScoreDetail.setActive(Constant.ACTIVE);

            studentCourseScoreDetailRepository.save(studentCourseScoreDetail);
        } else
            throw new CustomException(ResultEnum.EntityIsNullException.getMessage(), ResultEnum.EntityIsNullException.getCode());
    }

    public PageResultVO<StudentCourseScoreDetail> findByConditions(StudentCourseScoreDetailDto studentCourseScoreDetailDto) {
        Pageable pageable = PageRequest.of(studentCourseScoreDetailDto.getCurrentPage() - 1, studentCourseScoreDetailDto.getPageSize(), getSort());
        Page<StudentCourseScoreDetail> stuCourseScoreDetailPage = studentCourseScoreDetailRepository.findAll(new Specification<StudentCourseScoreDetail>() {

            @Override
            public Predicate toPredicate(Root<StudentCourseScoreDetail> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getStudentNo())) {
                    Path studentNo = root.get("studentNo");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(studentNo), "%" + studentCourseScoreDetailDto.getStudentNo().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getStudentName())) {
                    Path studentName = root.get("studentName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(studentName), "%" + studentCourseScoreDetailDto.getStudentName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getSelectedCourseNo())) {
                    Path selectedCourseNo = root.get("selectedCourseNo");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(selectedCourseNo), "%" + studentCourseScoreDetailDto.getSelectedCourseNo().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getCourseName())) {
                    Path courseName = root.get("courseName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(courseName), "%" + studentCourseScoreDetailDto.getCourseName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getAcademicYear())) {
                    Path academicYear = root.get("academicYear");
                    Predicate p = criteriaBuilder.equal(academicYear, studentCourseScoreDetailDto.getAcademicYear());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(studentCourseScoreDetailDto.getTerm())) {
                    Path term = root.get("term");
                    Predicate p = criteriaBuilder.equal(term, studentCourseScoreDetailDto.getTerm());
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

        PageResultVO<StudentCourseScoreDetail> pageResultVO = new PageResultVO<>(stuCourseScoreDetailPage.getContent(), stuCourseScoreDetailPage.getTotalElements());
        return pageResultVO;
    }

    public List<StudentCourseScoreDetail> findAllActive() {
        return studentCourseScoreDetailRepository.findByActive(Constant.ACTIVE);
    }

    @Transactional
    public void removeDetails(List<StudentCourseScoreDetail> detailList) {
        if (!detailList.isEmpty()) {
            detailList.forEach(detail -> detail.setActive(Constant.INACTIVE));
            studentCourseScoreDetailRepository.saveAll(detailList);
        } else
            throw new CustomException(ResultEnum.DeleteFailedException.getMessage(), ResultEnum.DeleteFailedException.getCode());
    }

    public void uploadDetailList(MultipartFile file) {
        if (null != file) {
            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                // Ignore the title row
                Row row = rowIterator.next();
                List<StudentCourseScoreDetail> detailList = new ArrayList<>();
                int rowIndex = 1;
                // for check organization name
                List<Student> studentList = studentRepository.findByActive(Constant.ACTIVE);
                List<Course> courseList = courseRepository.findByActive(Constant.ACTIVE);
                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    rowIndex += 1;
                    // student's necessary information can't be null
                    if (ExcelUtil.isFull(row, Constant.STUDENT_NO_INDEX, Constant.COURSE_TERM_INDEX)) {
                        StudentCourseScoreDetail detail = new StudentCourseScoreDetail();
                        String studentNo = ExcelUtil.getStringCellValue(row.getCell(Constant.STUDENT_NO_INDEX));
                        String studentName = ExcelUtil.getStringCellValue(row.getCell(Constant.STUDENT_NAME_INDEX));
                        String orgName = ExcelUtil.getStringCellValue(row.getCell(Constant.ORG_NAME_INDEX));
                        String className = ExcelUtil.getStringCellValue(row.getCell(Constant.CLASS_NAME_INDEX));
                        String major = ExcelUtil.getStringCellValue(row.getCell(Constant.MAJOR_NAME_INDEX));
                        // check student info
                        Student student = checkStudentInfo(studentList, studentNo, studentName, orgName, className, major);
                        if (null == student) {
                            throw new CustomException(String.format(ResultEnum.StudentInfoNotExistException.getMessage(), String.valueOf(rowIndex)), ResultEnum.StudentInfoNotExistException.getCode());
                        }
                        String selectedCourseNo = ExcelUtil.getStringCellValue(row.getCell(Constant.SELECTED_COURSE_NO_INDEX));
                        String courseCode = ExcelUtil.getStringCellValue(row.getCell(Constant.COURSE_CODE_INDEX));
                        String courseName = ExcelUtil.getStringCellValue(row.getCell(Constant.COURSE_NAME_INDEX));
                        String academicYear = ExcelUtil.getStringCellValue(row.getCell(Constant.COURSE_ACADEMIC_YEAR_INDEX));
                        String term = ExcelUtil.getStringCellValue(row.getCell(Constant.COURSE_TERM_INDEX));
                        // check course info
                        Course course = checkCourseInfo(courseList, selectedCourseNo, courseCode, courseName, academicYear, term);
                        if (null == course) {
                            throw new CustomException(String.format(ResultEnum.CourseInfoNotExistException.getMessage(), String.valueOf(rowIndex)), ResultEnum.CourseInfoNotExistException.getCode());
                        }
                        // copy same properties
                        BeanUtils.copyProperties(course, detail);
                        BeanUtils.copyProperties(student, detail);
                        // set other column
                        detail.setRetakeFlag(ExcelUtil.getStringCellValue(row.getCell(Constant.RETAKE_FLAG_INDEX)));
                        detail.setUsualScore(ExcelUtil.getStringCellValue(row.getCell(Constant.USUAL_SCORE_INDEX)));
                        detail.setMiddleScore(ExcelUtil.getStringCellValue(row.getCell(Constant.MIDDLE_SCORE_INDEX)));
                        detail.setEndScore(ExcelUtil.getStringCellValue(row.getCell(Constant.END_SCORE_INDEX)));
                        detail.setFinalScore(ExcelUtil.getStringCellValue(row.getCell(Constant.FINAL_SCORE_INDEX)));
                        detail.setLabScore(ExcelUtil.getStringCellValue(row.getCell(Constant.LAB_SCORE_INDEX)));
                        detail.setConvertScore(ExcelUtil.getStringCellValue(row.getCell(Constant.CONVERT_SCORE_INDEX)));
                        detail.setResitScore(ExcelUtil.getStringCellValue(row.getCell(Constant.RESIT_SCORE_INDEX)));
                        detail.setResitMemo(ExcelUtil.getStringCellValue(row.getCell(Constant.RESIT_SCORE_MEMO_INDEX)));
                        detail.setRepairScore(ExcelUtil.getStringCellValue(row.getCell(Constant.REPAIRE_SCORE_INDEX)));
                        detail.setMemo(ExcelUtil.getStringCellValue(row.getCell(Constant.MEMO_INDEX)));
                        detail.setCreateTime(new Date());
                        detail.setId(UUID.randomUUID().toString().replace("-", ""));
                        detail.setActive(Constant.ACTIVE);
                        detailList.add(detail);
                    } else
                        throw new CustomException(String.format(ResultEnum.StudentCourseDetailUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.StudentCourseDetailUploadIncomplete.getCode());
                }
                // do save -> 去重
                saveStudentCourseDetailList(detailList);
            }
        } else
            throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
    }

    private void saveStudentCourseDetailList(List<StudentCourseScoreDetail> detailList) {
        List<StudentCourseScoreDetail> existDetailList = studentCourseScoreDetailRepository.findByActive(Constant.ACTIVE);
        List<StudentCourseScoreDetail> duplicateList = new ArrayList<>();
        List<StudentCourseScoreDetail> insertList = new ArrayList<>();
        // 去重
        detailList.forEach(detail -> {
            Boolean isExist = checkDetailDuplicate(detail, existDetailList);
            if (isExist)
                duplicateList.add(detail);
            else
                insertList.add(detail);
        });
        // insert list
        if (!insertList.isEmpty()) {
            asyncInsertHandler(insertList);
        }
        if (!duplicateList.isEmpty())
            throw new CustomException(String.format(ResultEnum.StudentDetailUploadDuplicateException.getMessage(), insertList.size(), duplicateList.size()),
                    ResultEnum.StudentDetailUploadDuplicateException.getCode());
    }

    // 异步线程执行插入
    private void asyncInsertHandler(List<StudentCourseScoreDetail> detailList) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    insertDetailList(detailList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Transactional
    protected void insertDetailList(List<StudentCourseScoreDetail> detailList) {
        studentCourseScoreDetailRepository.saveAll(detailList);
    }

    private Boolean checkDetailDuplicate(StudentCourseScoreDetail detail, List<StudentCourseScoreDetail> existDetailList) {
        int size = existDetailList.size();
        for (int i = 0; i < size; i++) {
            StudentCourseScoreDetail existDetail = existDetailList.get(i);
            if (detail.getStudentNo().equals(existDetail.getStudentNo()) && detail.getSelectedCourseNo().equals(existDetail.getSelectedCourseNo())) {
                return true;
            }
        }
        return false;
    }

    private Course checkCourseInfo(List<Course> courseList, String selectedCourseNo, String courseCode, String courseName, String academicYear, String term) {
        Course course = null;
        int length = courseList.size();
        for (int i = 0; i < length; i++) {
            course = courseList.get(i);
            if (selectedCourseNo.equals(course.getSelectedCourseNo()) && courseCode.equals(course.getCourseCode()) && courseName.equals(course.getCourseName())
                    && academicYear.equals(course.getAcademicYear()) && term.equals(course.getTerm())) {
                return course;
            }
        }
        return null;
    }

    private Student checkStudentInfo(List<Student> studentList, String studentNo, String studentName, String orgName, String className, String major) {
        Student student = null;
        int length = studentList.size();
        for (int i = 0; i< length; i++) {
            student = studentList.get(i);
            if (studentNo.equals(student.getStudentNo()) && studentName.equals(student.getStudentName()) &&
                    orgName.equals(student.getOrgName()) && major.equals(student.getMajor()) && className.equals(student.getClassName()))
                return student;
        }
        return null;
    }
}
