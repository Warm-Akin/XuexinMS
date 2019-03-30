package com.zhbit.xuexin.dto;

public class ResumeDto {

    private String studentName;

    private String jobWant;

    private String schoolName;

    private String major;

    private Integer currentPage;

    private Integer pageSize;

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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
