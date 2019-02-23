package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_teacherinfo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teacher {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", length = 32)
    private String teacherId;

    @Column(name = "EMPLOY_NO", unique = true, length = 10, updatable = false)
    private String teacherNo; // 教师工号

    @Column(name = "EMPLOY_NAME", length = 64)
    private String teacherName; // 教师姓名

    @Column(name = "SEX", length = 2)
    private String sex;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY") // length = 7
    private Date birthday; // 出生日期

    @Column(name = "ORG_ID", length = 32)
    private String orgId; // 学院id

    @Column(name = "ORG_NAME", length = 100)
    private String orgName; //学院名称

    @Column(name = "DEPARTMENT", length = 100)
    private String department; // 系(k科室)

    @Column(name = "TELNO", length = 20)
    private String telNo; // '联系电话'

    @Column(name = "EMAIL", length = 50)
    private String email; // 'E_mail地址'

    @Column(name = "ADDRESS", length = 200)
    private String address; // '联系地址'

    @Column(name = "CATEGORY", length = 20)
    private String category; // '教职工类别'

    @Column(name = "EDUCATION", length = 20)
    private String education; // 学历

    @Column(name = "DEGREE", length = 20)
    private String degree; // '学位'

    @Column(name = "DUTY", length = 20)
    private String duty; // '职务'

    @Column(name = "ACDEMICTITLE", length = 20) // column name is wrong
    private String academicTitle; // '职称'

    @Column(name = "INVIGILATORFLAG", length = 2)
    private String invigilatorFlag; // '可否监考：T：可以；F：不可以'

    @Column(name = "RESEARCHDIRECTION", length = 100)
    private String researchDirection; // '研究方向'

    @Column(name = "INTRODUCE", columnDefinition = "text")
    private String introduce; // '教师简介'

    @Column(name = "MAJOR", length = 100)
    private String major; // '专业'

    @Column(name = "GRADUATE", length = 100)
    private String graduateSchool; // '毕业院校'

    @Column(name = "QUALIFICATIONFLAG", length = 2)
    private String qualificationFlag; // '教师资格标志：Y:有；N：无'

    @Column(name = "JOBSTATUS", length = 2)
    private String jobStatus; // '在职状态：Y:在职；N：离职'

    @Column(name = "TEACHER_LEVEL", length = 20)
    private String teacherLevel; // '教师级别'

    @Column(name = "ISLAB", length = 2)
    private String isLab; // '是否实验室人员：Y：是；N：否'

    @Column(name = "ISOUTHIRE", length = 2)
    private String isOutHire; // '是否外聘：Y：是；N：否'

    @Column(name = "POLITICALSTATUS", length = 20)
    private String politicalStatus; // 政治面貌

    @Column(name = "NATION", length = 50)
    private String nation; // 民族

    @CreatedDate
    @Column(name = "CREATE_TIME") // , length = 7
    private Date createTime; // '创建时间'

    @CreatedBy
    @Column(name = "CREATOR", length = 32)
    private String creator;

    @Column(name = "PASSWORD", length = 50)
    private String password; // '登录密码'

    @Column(name = "PHOTOPATH", length = 200)
    private String photopath;

    @Column(name = "PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    @Column(name = "ACTIVE")
    private Integer active;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId=teacherId;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo=teacherNo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName=teacherName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex=sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday=birthday;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId=orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName=orgName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department=department;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo=telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address=address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category=category;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education=education;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree=degree;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty=duty;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle=academicTitle;
    }

    public String getInvigilatorFlag() {
        return invigilatorFlag;
    }

    public void setInvigilatorFlag(String invigilatorFlag) {
        this.invigilatorFlag=invigilatorFlag;
    }

    public String getResearchDirection() {
        return researchDirection;
    }

    public void setResearchDirection(String researchDirection) {
        this.researchDirection=researchDirection;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce=introduce;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major=major;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool=graduateSchool;
    }

    public String getQualificationFlag() {
        return qualificationFlag;
    }

    public void setQualificationFlag(String qualificationFlag) {
        this.qualificationFlag=qualificationFlag;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus=jobStatus;
    }

    public String getTeacherLevel() {
        return teacherLevel;
    }

    public void setTeacherLevel(String teacherLevel) {
        this.teacherLevel=teacherLevel;
    }

    public String getIsLab() {
        return isLab;
    }

    public void setIsLab(String isLab) {
        this.isLab=isLab;
    }

    public String getIsOutHire() {
        return isOutHire;
    }

    public void setIsOutHire(String isOutHire) {
        this.isOutHire=isOutHire;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus=politicalStatus;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation=nation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator=creator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath=photopath;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId=parentOrgId;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active=active;
    }
}
