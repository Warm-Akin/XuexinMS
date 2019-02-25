package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_courseinfo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Course {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", length = 32)
    private String courseId;

    @Column(name = "COURSECODE", unique = true, length = 20)
    private String courseCode; // 课程代码

    @Column(name = "COURSENAME", length = 100)
    private String courseName; // 课程名称

    @Column(name = "ACADEMICYEAR", length = 20)
    private String academicYear; // 学年

    @Column(name = "TERM", length = 20)
    private String term; // 学期

    @Column(name = "EMPLOY_NO", length = 50)
    private String teacherNo; // 教师工号

    @Column(name = "EMPLOY_NAME", length = 64)
    private String teacherName; // 教师姓名

    @Column(name = "SELECTEDCOURSENO", length = 50)
    private String selectedCourseNo; // 选课课号

    @Column(name = "COURSETYPE", length = 20)
    private String courseType; // 课程性质

    @Column(name = "TOTALHOURS")
    private Double totalHours; // 总学时

    @Column(name = "LABHOURS")
    private Double labHours; // 实验学时

    @Column(name = "CLASSINFO", columnDefinition = "text")
    private String classInfo; // 教学班组成

    @Column(name = "STUDENTNUM")
    private Double studentNum; // 选课人数

    @Column(name = "CREDIT")
    private Double credit; // 学分：可根据总学时除以16进行换算得到该值

    @Column(name = "BELONGTO", length = 100)
    private String belongTo; // 课程归属：该值在导入的学生成绩明细中

    @Column(name = "MEMO", length = 200)
    private String memo;

    @CreatedDate
    @Column(name = "CREATE_TIME") //
    private Date createTime; // '创建时间'

    @CreatedBy
    @Column(name = "CREATOR", length = 32)
    private String creator;

    @Column(name = "PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    @Column(name = "ORG_ID", length = 32)
    private String orgId; // 学院id

    @Column(name = "ORG_NAME", length = 100)
    private String orgName; // 学院名称

    @Column(name = "MAJOR", length = 50)
    private String major;

    @Column(name = "MAJORCODE", length = 10)
    private String majorCode;

    @Column(name = "HBMC", length = 200)
    private String hbmc;

    @Column(name = "KKDW", length = 200)
    private String kkdw;

    @Column(name = "SKXQ", length = 100)
    private String skxq;

    @Column(name = "TXKLB", length = 100)
    private String txklb;

    @Column(name = "LIMITSTUDENTNUM")
    private Double limitStudentNum; // 限选学生总数

    @Column(name = "ACTIVE")
    private Integer active;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId=courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode=courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName=courseName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear=academicYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term=term;
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

    public String getSelectedCourseNo() {
        return selectedCourseNo;
    }

    public void setSelectedCourseNo(String selectedCourseNo) {
        this.selectedCourseNo=selectedCourseNo;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType=courseType;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours=totalHours;
    }

    public Double getLabHours() {
        return labHours;
    }

    public void setLabHours(Double labHours) {
        this.labHours=labHours;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo=classInfo;
    }

    public Double getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Double studentNum) {
        this.studentNum=studentNum;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit=credit;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo=belongTo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo=memo;
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

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId=parentOrgId;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major=major;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode=majorCode;
    }

    public String getHbmc() {
        return hbmc;
    }

    public void setHbmc(String hbmc) {
        this.hbmc=hbmc;
    }

    public String getKkdw() {
        return kkdw;
    }

    public void setKkdw(String kkdw) {
        this.kkdw=kkdw;
    }

    public String getSkxq() {
        return skxq;
    }

    public void setSkxq(String skxq) {
        this.skxq=skxq;
    }

    public String getTxklb() {
        return txklb;
    }

    public void setTxklb(String txklb) {
        this.txklb=txklb;
    }

    public Double getLimitStudentNum() {
        return limitStudentNum;
    }

    public void setLimitStudentNum(Double limitStudentNum) {
        this.limitStudentNum=limitStudentNum;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
