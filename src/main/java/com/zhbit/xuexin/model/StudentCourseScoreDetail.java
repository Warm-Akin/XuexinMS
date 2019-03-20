package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_courseinfo_students")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentCourseScoreDetail implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", length = 32)
    private String id;

    @Column(name = "COURSECODE", length = 20)
    private String courseCode;

    @Column(name = "COURSENAME", length = 100)
    private String courseName;

    @Column(name = "STU_ID", length = 32)
    private String studentId;

    @Column(name = "STUDENTNO", nullable = false, length = 16)
    private String studentNo;

    @Column(name = "STUNAME", length = 20)
    private String studentName;

    @Column(name = "ORG_ID", length = 32)
    private String orgId;

    @Column(name = "ORG_NAME", length = 100)
    private String orgName;

    @Column(name = "CLASSNAME", length = 100)
    private String className;

    @Column(name = "MAJORCODE", length = 10)
    private String majorCode;

    @Column(name = "MAJOR", length = 100)
    private String major;

    @Column(name = "RETAKEFLAG", length = 2)
    private String retakeFlag;

    @Column(name ="USUALSCORE", length = 20)
    private String usualScore;

    @Column(name ="MIDDLESCORE", length = 20)
    private String middleScore;

    @Column(name ="ENDSCORE", length = 20)
    private String endScore;

    @Column(name ="LABSCORE", length = 20)
    private String labScore;

    @Column(name ="FINALSCORE", length = 20)
    private String finalScore; // 总评成绩

    @Column(name ="CONVERTSCORE", length = 20)
    private String convertScore; // 折算成绩

    @Column(name ="RESITSCORE", length = 20)
    private String resitScore; // 补考成绩

    @Column(name ="RESITMEMO", length = 200)
    private String resitMemo; // 补考成绩备注

    @Column(name ="REPAIRSCORE", length = 20)
    private String repairScore; // 重修成绩

    @Column(name ="GRADEPOINT", precision = 5)
    private Double gradePoint;

    @Column(name ="MEMO", length = 200)
    private String memo;

    @Temporal(TemporalType.DATE)
    @Column(name ="CREATE_TIME", length = 7)
    private Date createTime;

    @Column(name ="CREATOR", length = 32)
    private String creator;

    @Column(name ="PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    @Column(name = "TOTALHOURS")
    private Integer totalHours;

    // todo 2017.03.29添加
    @Column(name ="GRADE" ,length = 40)
    private String grade;

    @Column(name ="ACADEMICYEAR", length = 20)
    private String academicYear;

    @Column(name ="TERM", length = 20)
    private String term;

    @Column(name ="EMPLOY_NO", length = 50)
    private String teacherNo; //教师工号

    @Column(name ="EMPLOY_NAME", length = 64)
    private String teacherName; //教师姓名

    @Column(name ="SELECTEDCOURSENO", length = 50)
    private String selectedCourseNo;

    @Column(name ="COURSETYPE", length = 20)
    private String courseType;

    @Column(name ="CREDIT", length = 4)
    private Double credit;

    @Column(name = "ACTIVE")
    private Integer active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getRetakeFlag() {
        return retakeFlag;
    }

    public void setRetakeFlag(String retakeFlag) {
        this.retakeFlag = retakeFlag;
    }

    public String getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(String usualScore) {
        this.usualScore = usualScore;
    }

    public String getMiddleScore() {
        return middleScore;
    }

    public void setMiddleScore(String middleScore) {
        this.middleScore = middleScore;
    }

    public String getEndScore() {
        return endScore;
    }

    public void setEndScore(String endScore) {
        this.endScore = endScore;
    }

    public String getLabScore() {
        return labScore;
    }

    public void setLabScore(String labScore) {
        this.labScore = labScore;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getConvertScore() {
        return convertScore;
    }

    public void setConvertScore(String convertScore) {
        this.convertScore = convertScore;
    }

    public String getResitScore() {
        return resitScore;
    }

    public void setResitScore(String resitScore) {
        this.resitScore = resitScore;
    }

    public String getResitMemo() {
        return resitMemo;
    }

    public void setResitMemo(String resitMemo) {
        this.resitMemo = resitMemo;
    }

    public String getRepairScore() {
        return repairScore;
    }

    public void setRepairScore(String repairScore) {
        this.repairScore = repairScore;
    }

    public Double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(Double gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSelectedCourseNo() {
        return selectedCourseNo;
    }

    public void setSelectedCourseNo(String selectedCourseNo) {
        this.selectedCourseNo = selectedCourseNo;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}