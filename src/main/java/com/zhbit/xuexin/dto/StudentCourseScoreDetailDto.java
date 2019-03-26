package com.zhbit.xuexin.dto;

public class StudentCourseScoreDetailDto {

    private String selectedCourseNo;

    private String studentNo;

    private String studentName;

    private String courseName;

    private String academicYear;

    private String term;

    private Integer currentPage;

    private Integer pageSize;

    public String getSelectedCourseNo() {
        return selectedCourseNo;
    }

    public void setSelectedCourseNo(String selectedCourseNo) {
        this.selectedCourseNo = selectedCourseNo;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
