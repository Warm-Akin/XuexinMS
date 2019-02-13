package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "STU_ID", length = 32)
    private String stuId;

    @Column(name = "STUDENTNO", unique = true, length = 16)
    private String studentNo;

    @Column(name = "STUNAME", length = 20)
    private String studentName;

    @Column(name = "SEX", length = 2)
    private String sex;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY", length = 7)
    private Date birthday;

    @Column(name = "POLITICALSTATUS", length = 20)
    private String politicalStatus; // 政治面貌

    @Column(name = "NATION", length = 50)
    private String nation; // 民族

    @Column(name = "NATIVEPLACE", length = 20)
    private String nativePlace; // 籍贯

    @Column(name = "FROM_PLACE", length = 100)
    private String fromPlace; // 来源地区

    @Column(name = "IDCARDNO", length = 18)
    private String idcardNo;

    @Column(name = "ORG_ID", length = 32)
    private String orgId; // 学院id

    @Column(name = "ORG_NAME", length = 100)
    private String orgName; //学院名称

    @Column(name = "DEPARTMENT", length = 100)
    private String department; // '系'

    @Column(name = "MAJOR", length = 100)
    private String major; // 专业

    @Column(name = "MAJOR_CODE", length = 10)
    private String majorCode;

    @Column(name = "MAJORFIELD", length = 100)
    private String majorField; // 专业方向

    @Column(name = "MAJORCATEGORIES", length = 100)
    private String majorCategories; // '专业类别'

    @Column(name = "CULTIVATEDIRECTION", length = 100)
    private String cultivateDirection; // '培育方向'

    @Column(name = "CLASSNAME", length = 100)
    private String className; // 班级名称

    @Column(name = "EDUCATIONSYSTEM", precision = 22, scale = 0)
    private Integer educationSystem; // '学制'

    @Column(name = "SCHOOLINGLENGTH", precision = 22, scale = 0)
    private Integer schoolingLength; // '学习年限'

    @Temporal(TemporalType.DATE)
    @Column(name = "ACCEPTANCEDATE", length = 7)
    private Date acceptanceDate; // '入学日期'

    @Column(name = "MIDDLESCHOOL", length = 100)
    private String middleSchool; // '毕业中学'

    @Column(name = "MOBILENO", length = 11)
    private String mobileNo; // 本人电话

    @Column(name = "FAMILYTELNO", length = 20)
    private String familyTelNo; // 家庭电话

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "POSTCODE", length = 6)
    private String postcode;

    @Column(name = "TRAVELRANGE", length = 50)
    private String travelRange; // '乘车区间'

    @Column(name = "ADDRESS", length = 200)
    private String address; // '家庭地址'

    @Column(name = "SKILL", length = 500)
    private String skill; // 特长

    @Column(name = "SELFDESCRIPTION", length = 500)
    private String selfDescription; // 自我介绍

    @Column(name = "AWARDS", length = 500)
    private String awards; // '所获奖励'

    @Column(name = "SCHOOLSTATUS", length = 20)
    private String schoolStatus; // '学籍状态'

    @Column(name = "DQSZJ", length = 20)
    private String dqszj;

    @Column(name = "PHOTOPATH", length = 200)
    private String photopath;

    @Column(name = "GRADUATEFLAG", length = 2)
    private String graduateFlag; // '毕业审核标志：\r\nY：毕业审核通过\nN：审核不通过'

    @Column(name = "ALUMNIFLAG", length = 2)
    private String alumniFlag; // '导入校友会标志：预留字段\nY：已导入\nN：未导入',

    @CreatedDate
    @Column(name = "CREATE_TIME", length = 7)
    private Date createTime;

    @CreatedBy
    @Column(name = "CREATOR", length = 32)
    private String creator;

    @Column(name = "PASSWORD", length = 50)
    private String password; // '登录密码'

    @Column(name = "PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    @Column(name = "SCORE")
    private double score;

    @Column(name = "GRADE", length = 20)
    private String grade;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajorField() {
        return majorField;
    }

    public void setMajorField(String majorField) {
        this.majorField = majorField;
    }

    public String getMajorCategories() {
        return majorCategories;
    }

    public void setMajorCategories(String majorCategories) {
        this.majorCategories = majorCategories;
    }

    public String getCultivateDirection() {
        return cultivateDirection;
    }

    public void setCultivateDirection(String cultivateDirection) {
        this.cultivateDirection = cultivateDirection;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getEducationSystem() {
        return educationSystem;
    }

    public void setEducationSystem(Integer educationSystem) {
        this.educationSystem = educationSystem;
    }

    public Integer getSchoolingLength() {
        return schoolingLength;
    }

    public void setSchoolingLength(Integer schoolingLength) {
        this.schoolingLength = schoolingLength;
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getMiddleSchool() {
        return middleSchool;
    }

    public void setMiddleSchool(String middleSchool) {
        this.middleSchool = middleSchool;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFamilyTelNo() {
        return familyTelNo;
    }

    public void setFamilyTelNo(String familyTelNo) {
        this.familyTelNo = familyTelNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTravelRange() {
        return travelRange;
    }

    public void setTravelRange(String travelRange) {
        this.travelRange = travelRange;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public String getDqszj() {
        return dqszj;
    }

    public void setDqszj(String dqszj) {
        this.dqszj = dqszj;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getGraduateFlag() {
        return graduateFlag;
    }

    public void setGraduateFlag(String graduateFlag) {
        this.graduateFlag = graduateFlag;
    }

    public String getAlumniFlag() {
        return alumniFlag;
    }

    public void setAlumniFlag(String alumniFlag) {
        this.alumniFlag = alumniFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}