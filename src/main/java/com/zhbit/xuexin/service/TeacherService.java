package com.zhbit.xuexin.service;

import com.zhbit.xuexin.common.constant.Constant;
import com.zhbit.xuexin.common.exception.CustomException;
import com.zhbit.xuexin.common.response.PageResultVO;
import com.zhbit.xuexin.common.response.ResultEnum;
import com.zhbit.xuexin.common.util.DateUtil;
import com.zhbit.xuexin.common.util.ExcelUtil;
import com.zhbit.xuexin.common.util.SecurityUtil;
import com.zhbit.xuexin.dto.TeacherDto;
import com.zhbit.xuexin.model.Organization;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    JdbcTemplate jdbcTemplate;

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
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherNo), "%" + teacherDto.getTeacherNo() + "%");
                    predicateList.add(p);
                }
                if (!StringUtils.isEmpty(teacherDto.getTeacherName())) {
                    Path teacherName = root.get("teacherName");
                    Predicate p = criteriaBuilder.like(criteriaBuilder.upper(teacherName), "%" + teacherDto.getTeacherName() + "%");
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
            Workbook wb = ExcelUtil.getWorkbookFromFile(file);
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator=sheet.rowIterator();
                // Ignore the title row
                Row row = rowIterator.next();
                List<Teacher> teacherList=new ArrayList<>();
                int rowIndex=1;
                // for check organization name
                List<Organization> organizationList = organizationService.findAllActive();
                while (rowIterator.hasNext()) {
                    row = rowIterator.next();
                    rowIndex += 1;
                    // teacher's necessary information can't be null
                    if (ExcelUtil.isFull(row, Constant.INDEX_TEACHER_NO, Constant.INDEX_TEACHER_NAME)) {
                        Teacher teacher = new Teacher();
                        teacher.setTeacherNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_NO)));
                        teacher.setTeacherName(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_NAME)));
                        String sex = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_SEX));
                        if (!StringUtils.isEmpty(sex) && "男".equals(sex)) {
                            sex = "0";
                        } else if (!StringUtils.isEmpty(sex) && "女".equals(sex)) {
                            sex = "1";
                        } else {
                            sex = "";
                        }
                        teacher.setSex(sex);
                        Date birthday = !"".equals(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_BIRTHDAY))) ? DateUtil.formatDate(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_BIRTHDAY)).replaceAll("-", "")) : null;
                        teacher.setBirthday(birthday);
                        // check orgName
                        String orgName = ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_ORGNAME));
                        String orgId = organizationService.checkOrgNameReturnOrgId(orgName, organizationList);
                        if (null != orgId && !StringUtils.isEmpty(orgId)) {
                            teacher.setOrgName(orgName);
                            teacher.setOrgId(orgId);
                        } else
                            throw new CustomException(String.format(ResultEnum.OrganizationNameNotFoundException.getMessage(), String.valueOf(rowIndex)), ResultEnum.OrganizationNameNotFoundException.getCode());
                        teacher.setTelNo(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_TELNO)));
                        teacher.setEmail(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_EAMIL)));
                        teacher.setAddress(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_ADDRESS)));
                        teacher.setCategory(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_CATEGORY)));
                        teacher.setEducation(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_EDUCATION)));
                        teacher.setDegree(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_DEGREE)));
                        teacher.setDuty(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_DUTY)));
                        teacher.setAcademicTitle(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_ACADEMICTITLE)));
                        teacher.setInvigilatorFlag(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_INVIGILATORFLAG)));
                        teacher.setResearchDirection(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_RESEARCHDIRECTION)));
                        teacher.setIntroduce(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_INTRODUCE)));
                        teacher.setMajor(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_MAJOR)));
                        teacher.setGraduateSchool(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_GRADUATE_SCHOOL)));
                        teacher.setQualificationFlag(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_QUALIFICATIONFLAG)));
                        teacher.setJobStatus(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_JOBSTATUS)));
                        teacher.setIsLab(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_ISLAB)));
                        teacher.setIsOutHire(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_ISOUTHIRE)));
                        teacher.setPoliticalStatus(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_POLITICALSTATUS)));
                        teacher.setNation(ExcelUtil.getStringCellValue(row.getCell(Constant.INDEX_TEACHER_NATION)));
                        teacherList.add(teacher);
                    } else
                        throw new CustomException(String.format(ResultEnum.TeacherUploadIncomplete.getMessage(), String.valueOf(rowIndex)), ResultEnum.TeacherUploadIncomplete.getCode());
                }
                // do filter List before save
                saveTeacherListForUpload(teacherList);
            } else
            throw new CustomException(ResultEnum.FileIsNullException.getMessage(), ResultEnum.FileIsNullException.getCode());
        }
    }

    private void saveTeacherListForUpload(List<Teacher> teacherList) {
        List<Teacher> teacherExistList = teacherRepository.findAll();
        List<Teacher> duplicateTeacherNoList = new ArrayList<>();
        List<Teacher> newInsertTeacherList = new ArrayList<>();
        teacherList.forEach(teacher -> {
            Boolean isExist = compareTeacherNo(teacher, teacherExistList);
            if (isExist)
                duplicateTeacherNoList.add(teacher);
            else {
                // set ID and ACTIVE
                teacher.setTeacherId(UUID.randomUUID().toString().replace("-", ""));
                teacher.setPassword(SecurityUtil.GetMD5Code(teacher.getTeacherNo()));
                teacher.setActive(Constant.ACTIVE);
                newInsertTeacherList.add(teacher);
            }
        });
        // insert the new records
        if (!newInsertTeacherList.isEmpty())
            insertTeachers(newInsertTeacherList);
        // handle duplicateTeacherNoList
        if (!duplicateTeacherNoList.isEmpty()) {
            List<String> duplicateTeacherNo = new ArrayList<>();
            duplicateTeacherNoList.forEach(teacher -> {
                duplicateTeacherNo.add(teacher.getTeacherNo());
            });
            throw new CustomException(String.format(ResultEnum.TeacherNoUploadException.getMessage(), newInsertTeacherList.size(), duplicateTeacherNo),
                    ResultEnum.TeacherNoUploadException.getCode());
        }
    }

    private Boolean compareTeacherNo(Teacher teacher, List<Teacher> teacherExistList) {
        int listSize = teacherExistList.size();
        Boolean isExist = false;
        for (int i = 0; i < listSize; i++) {
            if (teacher.getTeacherNo().trim().equals(teacherExistList.get(i).getTeacherNo())) {
                isExist = true;
                break;
            }
        }
        // 当前工号不存在，则加入现有的教师List
        if (!isExist)
            teacherExistList.add(teacher);
        return isExist;
    }

    private int insertTeachers(List<Teacher> teachers) {
        String sql = "INSERT INTO t_teacherinfo(ID, EMPLOY_NO, EMPLOY_NAME, SEX, BIRTHDAY, ORG_NAME, TELNO ,EMAIL, ADDRESS, CATEGORY, EDUCATION, DEGREE," +
                " DUTY, ACDEMICTITLE, INVIGILATORFLAG, RESEARCHDIRECTION, INTRODUCE, MAJOR, GRADUATE, QUALIFICATIONFLAG, JOBSTATUS, ISLAB, ISOUTHIRE," +
                " POLITICALSTATUS, NATION, ACTIVE, ORG_ID, PASSWORD) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Teacher teacher = teachers.get(i);
                preparedStatement.setString(1, teacher.getTeacherId());
                preparedStatement.setString(2, teacher.getTeacherNo());
                preparedStatement.setString(3, teacher.getTeacherName());
                preparedStatement.setString(4, teacher.getSex());
                if (null != teacher.getBirthday())
                    preparedStatement.setDate(5, new java.sql.Date(teacher.getBirthday().getTime()));
                else
                    preparedStatement.setDate(5, null);
                preparedStatement.setString(6, teacher.getOrgName());
                preparedStatement.setString(7, teacher.getTelNo());
                preparedStatement.setString(8, teacher.getEmail());
                preparedStatement.setString(9, teacher.getAddress());
                preparedStatement.setString(10, teacher.getCategory());
                preparedStatement.setString(11, teacher.getEducation());
                preparedStatement.setString(12, teacher.getDegree());
                preparedStatement.setString(13, teacher.getDuty());
                preparedStatement.setString(14, teacher.getAcademicTitle());
                preparedStatement.setString(15, teacher.getInvigilatorFlag());
                preparedStatement.setString(16, teacher.getResearchDirection());
                preparedStatement.setString(17, teacher.getIntroduce());
                preparedStatement.setString(18, teacher.getMajor());
                preparedStatement.setString(19, teacher.getGraduateSchool());
                preparedStatement.setString(20, teacher.getQualificationFlag());
                preparedStatement.setString(21, teacher.getJobStatus());
                preparedStatement.setString(22, teacher.getIsLab());
                preparedStatement.setString(23, teacher.getIsOutHire());
                preparedStatement.setString(24, teacher.getPoliticalStatus());
                preparedStatement.setString(25, teacher.getNation());
                preparedStatement.setInt(26, teacher.getActive());
                preparedStatement.setString(27, teacher.getOrgId());
                preparedStatement.setString(28, teacher.getPassword());
            }

            @Override
            public int getBatchSize() {
                return teachers.size();
            }
        }).length;
    }

    @Transactional
    public void delete(List<Teacher> teachers) {
        if (!teachers.isEmpty()) {
            teachers.forEach(teacher -> teacher.setActive(Constant.INACTIVE));
            teacherRepository.saveAll(teachers);
        } else
            throw new CustomException(ResultEnum.DeleteFailedException.getMessage(), ResultEnum.DeleteFailedException.getCode());
    }
}
