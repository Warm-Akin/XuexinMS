package com.zhbit.xuexin.dto;

public class TeacherDto {

    private String teacherNo;

    private String teacherName;

    private String sex;

    private String orgName;

    private String academicTitle; // 职称

    private String qualificationFlag; // '教师资格标志：Y:有；N：无'

    private String jobStatus; // '在职状态：Y:在职；N：离职'

    private String isOutHire; // '是否外聘：Y：是；N：否'

    private Integer currentPage;

    private Integer pageSize;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName=orgName;
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

    public String getIsOutHire() {
        return isOutHire;
    }

    public void setIsOutHire(String isOutHire) {
        this.isOutHire=isOutHire;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage=currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize=pageSize;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }
}
