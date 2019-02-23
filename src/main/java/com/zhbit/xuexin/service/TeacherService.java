package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.ExcelUtil;
import com.zhbit.xuexin.dto.TeacherDto;
import com.zhbit.xuexin.model.Teacher;
import com.zhbit.xuexin.repository.TeacherRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    public PageResultVO<Teacher> findAll(Integer page, Integer pageSize) {
        // page's value start at '0'
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, getSort());
        Page<Teacher> teacherPage = (Page<Teacher>) teacherRepository.findAll(new Specification<Teacher>() {

            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        PageResultVO<Teacher> pageResultVO = new PageResultVO<>(teacherPage.getContent(), teacherPage.getTotalElements());
        return pageResultVO;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    private Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "teacherNo"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "teacherName"));
        return new Sort(orders);
    }

    @Transactional
    public void handleSave(Teacher teacher) {
        if (null != teacher) {
            if (StringUtils.isEmpty(teacher.getTeacherId())) {
                // add
                String teacherNo = teacher.getTeacherNo();
                // todo jpa查找对象不为null
                Teacher isTeacherExist = !StringUtils.isEmpty(teacherNo) ? teacherRepository.findByTeacherNo(teacherNo) : null;
                if (isTeacherExist == null) {
                    teacher.setCreateTime(new Date());
                    teacher.setActive(Constant.ACTIVE);
                } else {
                    throw new CustomException(ResultEnum.TeacherNoDuplicatedException.getMessage(), ResultEnum.TeacherNoDuplicatedException.getCode());
                }

            } else {
                // update
                Teacher currentTeacher = teacherRepository.findById(teacher.getTeacherId()).orElse(null);
                // set value for not allow modify Columns
                teacher.setTeacherNo(currentTeacher.getTeacherNo());
            }
            teacherRepository.save(teacher);
        }
    }

    public PageResultVO<Teacher> findByConditions(TeacherDto teacherDto) {
        Pageable pageable = PageRequest.of(teacherDto.getCurrentPage() - 1, teacherDto.getPageSize(), getSort());
        Page<Teacher> teacherPage = teacherRepository.findAll(new Specification<Teacher>() {

            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(teacherDto.getTeacherNo())) {
                    Path teacherNo = root.get("teacherNo");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherNo), "%" + teacherDto.getTeacherNo().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getTeacherName())) {
                    Path teacherName = root.get("teacherName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherName), "%" + teacherDto.getTeacherName().toUpperCase() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getSex())) {
                    Path sex = root.get("sex");
                    Predicate p = criteriaBuilder.equal(sex, teacherDto.getSex());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getOrgName())) {
                    Path orgName = root.get("orgName");
                    Predicate p = criteriaBuilder.equal(orgName, teacherDto.getOrgName().trim());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getQualificationFlag())) {
                    Path qualificationFlag = root.get("qualificationFlag");
                    Predicate p = criteriaBuilder.equal(qualificationFlag, teacherDto.getQualificationFlag());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getJobStatus())) {
                    Path jobStatus = root.get("jobStatus");
                    Predicate p = criteriaBuilder.equal(jobStatus, teacherDto.getJobStatus());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getAcademicTitle())) {
                    Path academicTitle = root.get("academicTitle");
                    Predicate p = criteriaBuilder.equal(academicTitle, teacherDto.getAcademicTitle().trim());
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getIsOutHire())) {
                    Path isOutHire = root.get("isOutHire");
                    Predicate p = criteriaBuilder.equal(isOutHire, teacherDto.getIsOutHire());
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

        PageResultVO<Teacher> pageResultVO = new PageResultVO<>(teacherPage.getContent(), teacherPage.getTotalElements());
        return pageResultVO;
    }

    public void uploadTeacherList(MultipartFile file) {
        if (null != file) {
            // todo import
            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
            if (wb != null) {
                Sheet sheet=wb.getSheetAt(0);
                Iterator<Row> rowIterator=sheet.rowIterator();
                // Ignore the title row
                Row row=rowIterator.next();
                List<Teacher> teacherList=new ArrayList<>();
                int rowIndex=1;
                while (rowIterator.hasNext()) {
                    row=rowIterator.next();
                    rowIndex+=1;
                    // student's necessary information can't be null
//                    if (ExcelUtil.isFull(row, Constant.INDEX_STUDENT_NO, Constant.INDEX_CLASSNAME)) {
//                        Student student = new Student();
//                        student.setStudentNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_NO)));
//                        student.setStudentName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_NAME)));
//                        student.setSex(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_STUDENT_SEX)));
//                        student.setIdcardNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ID_CARD_NO)));
//                        student.setOrgName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ORGANIZATION_NAME)));
//                        // todo 学院(判断是否存在)
//                        student.setMajor(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MAJOR_NAME)));
//                        student.setMajorCategories(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MAJOR_CATEGORY)));
//                        student.setClassName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_CLASSNAME)));
//                        // set unnecessary columns
//                        student.setPoliticalStatus(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_POLITICAL_STATUS)));
//                        student.setNation(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_NATION)));
//                        student.setNativePlace(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_NATIVE_PLACE)));
//                        student.setFromPlace(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_FROM_PLACE)));
//                        student.setEducationSystem(Integer.parseInt(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_EDUCATION_SYSTEM))));
//                        student.setSchoolingLength(Integer.parseInt(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_SCHOOLING_LENGTH))));
//                        student.setCultivateDirection(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_CULTIVATE_DIRECTION)));
//                        student.setAcceptanceDate(DateUtil.formatDate(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ACCEPTANCE_DATE))));
//                        student.setMiddleSchool(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MIDDLE_SCHOOL)));
//                        student.setEmail(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_EMAIL)));
//                        student.setMobileNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_MOBILE_NO)));
//                        student.setFamilyTelNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_FAMILY_TEL_NO)));
//                        student.setPostcode(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_POST_CODE)));
//                        student.setTravelRange(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TRAVEL_RANGE)));
//                        student.setAddress(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_ADDRESS)));
//                        student.setSkill(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_SKILL)));
//                        // Computed attribute -> Birthday, Grade
//                        student.setBirthday(DateUtil.formatDate(student.getIdcardNo().substring(6, 13)));
//                        student.setGrade(Constant.GRADE_PREFIX + student.getStudentNo().substring(0, 2));
//                        studentList.add(student);
//                    } else
//                        throw new CustomException(String.format(ResultEnum.StudentUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.StudentUploadIncomplete.getCode());
                }
//                // do save in jdbcTemplate -> 去重
//                // todo
//                saveStudentListForUpload(studentList);
            }
//        } else
//            throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
        }
    }

}
