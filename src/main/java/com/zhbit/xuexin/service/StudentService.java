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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

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
            student.setBirthday(DateUtil.formatDate(student.getIdcardNo().substring(6, 14)));
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
                        // Computed attribute -> Birthday
                        student.setBirthday(DateUtil.formatDate(student.getIdcardNo().substring(6, 14)));
                        studentList.add(student);
                    } else
                        throw new CustomException(String.format(ResultEnum.StudentUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.StudentUploadIncomplete.getCode());
                }
                // do save in jdbcTemplate -> 去重
                // todo
            }
        } else
            throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());


//        if (file != null) {
//            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
//            if (wb != null) {
//                Sheet sheet = wb.getSheetAt(0);
//                Iterator<Row> rowIterator = sheet.rowIterator();
//                Row row = null;
//                // Ignore the title row
//                rowIterator.next();
//                List<ESGVendor> esgVendorList = new ArrayList<>();
//                int rowIndex = 1;
//                while (rowIterator.hasNext()) {
//                    row = rowIterator.next();
//                    if (ExcelUtil.isFull(row, Constant.INDEX_VENDOR_CODE, Constant.INDEX_VENDOR_REQUIRED)) {
//                        String vendorCode = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_CODE));
//                        String vendorName = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_NAME));
//                        String email = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_EMAIL));
//                        String region = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_REGION));
//                        String applicant = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_APPLICANT));
//                        String company = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_COMPANY));
//                        String strRequired = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_VENDOR_REQUIRED));
//                        ESGVendor esgVendor = new ESGVendor();
//                        esgVendor.setVendorCode(vendorCode);
//                        esgVendor.setVendorName(vendorName);
//                        esgVendor.setEmail(email);
//                        esgVendor.setRegion(region);
//                        esgVendor.setApplicant(applicant);
//                        esgVendor.setCompany(company);
//                        if (Constant.Y.equals(strRequired.toUpperCase()) || Constant.YES.equals(strRequired.toUpperCase())) {
//                            esgVendor.setRequired(1);
//                        } else {
//                            esgVendor.setRequired(0);
//                        }
//                        esgVendorList.add(esgVendor);
//                    } else {
//                        //some columns of the file is empty
//                        throw new CustomeException(String.format(ResultEnum.VendorUploadIncomplete.getMessage(), rowIndex), ResultEnum.VendorUploadIncomplete.getCode());
//                    }
//                    rowIndex += 1;
//                }
//                // save
//                saveVendorWithRewrite(esgVendorList, userAuditor);
//            } else // WorkBook is null
//                throw new CustomeException(ResultEnum.WorkBookIsNullException.getMessage(), ResultEnum.WorkBookIsNullException.getCode());
//        } else // file is null
//            throw new CustomeException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
    }
}