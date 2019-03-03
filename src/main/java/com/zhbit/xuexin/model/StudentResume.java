package com.zhbit.xuexin.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_student_resume")
public class StudentResume {

//    private static final long serialVersionUID = 5192902348951904950L;

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "STUDENTNO", unique = true, nullable = false, length = 32)
    private String studentNo;

    @Column(name = "STUNAME", length = 10)
    private String studentName;

    @Column(name = "JOBWANT", length = 32)
    private String jobWant;

    @Column(name = "MAJOR", length = 32)
    private String major;

    @Column(name = "EMAIL", length = 32)
    private String email;

    @Column(name = "PHOTOPATH", length = 255)
    private String photoPath;

    @Column(name = "MOBILENO", length = 32)
    private String mobileNo;

    @Column(name = "SELFEVALUATION", length = 255)
    private String selfEvaluation;

    @Column(name = "ORG_NAME", length = 32)
    private String orgName;

    @Column(name = "SCHOOLNAME", length = 32)
    private String schoolName;

    @Column(name = "MAJORINFO", length = 255)
    private String majorInfo;

    @Column(name = "SKILLONE", length = 32)
    private String skillOne;

    @Column(name = "ONEINFO", length = 255)
    private String oneInfo;

    @Column(name = "SKILLTWO", length = 32)
    private String skillTwo;

    @Column(name = "TWOINFO", length = 255)
    private String twoInfo;

    @Column(name = "SKILLTHREE", length = 32)
    private String skillThree;

    @Column(name = "THREEINFO", length = 255)
    private String threeInfo;

    @Column(name = "ENGLISHLEVEL", length = 32)
    private String englishLevel;

    @Column(name = "SOFTWARESKILLS", length = 255)
    private String softwareSkills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getJobWant() {
        return jobWant;
    }

    public void setJobWant(String jobWant) {
        this.jobWant = jobWant;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(String selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajorInfo() {
        return majorInfo;
    }

    public void setMajorInfo(String majorInfo) {
        this.majorInfo = majorInfo;
    }

    public String getSkillOne() {
        return skillOne;
    }

    public void setSkillOne(String skillOne) {
        this.skillOne = skillOne;
    }

    public String getOneInfo() {
        return oneInfo;
    }

    public void setOneInfo(String oneInfo) {
        this.oneInfo = oneInfo;
    }

    public String getSkillTwo() {
        return skillTwo;
    }

    public void setSkillTwo(String skillTwo) {
        this.skillTwo = skillTwo;
    }

    public String getTwoInfo() {
        return twoInfo;
    }

    public void setTwoInfo(String twoInfo) {
        this.twoInfo = twoInfo;
    }

    public String getSkillThree() {
        return skillThree;
    }

    public void setSkillThree(String skillThree) {
        this.skillThree = skillThree;
    }

    public String getThreeInfo() {
        return threeInfo;
    }

    public void setThreeInfo(String threeInfo) {
        this.threeInfo = threeInfo;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public String getSoftwareSkills() {
        return softwareSkills;
    }

    public void setSoftwareSkills(String softwareSkills) {
        this.softwareSkills = softwareSkills;
    }
}
