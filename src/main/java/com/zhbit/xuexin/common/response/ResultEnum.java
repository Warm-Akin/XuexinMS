package com.zhbit.xuexin.common.response;

import com.zhbit.xuexin.common.constant.HttpCode;

public enum ResultEnum {

    UnknownException(HttpCode.ERROR, "服务器异常！请重试或联系管理员"),
    EntityIsNullException("10001", "信息为空，保存失败，请检查后重试"),
    AccountInvalidException("10001", "用户名或密码错误，请检查后重试"),
    StudentNoDuplicatedException("10001", "学号信息重复，保存失败"),
    FileIsNullException("10001", "文件为空，请重试"),
    StudentUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    OrganizationNameNotFoundException("10001", "位于[第 %s 行]的学院名称信息不存在，请检查后重试"),
    TeacherUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    StudentNoUploadException("10001", "上传成功共有%s条记录，但以下学号的学生信息已经存在： %s, 故不允许再次添加"),
    TeacherNoUploadException("10001", "上传成功共有%s条记录，但以下工号的教师信息已经存在： %s, 故不允许再次添加"),
    CourseCodeUploadException("10001", "上传成功共有%s条记录，但以下课程代码的信息已经存在： %s, 故不允许再次添加"),
    TeacherNoDuplicatedException("10001", "教师工号信息重复，保存失败"),
    CourseCodeDuplicatedException("10001", "学号信息重复，保存失败"),
    DeleteFailedException("10001", "删除记录失败，请检查所提交的数据内容"),
    CourseInfoError("10001", "位于[第 %s 行]的限选总人数和选课人数不符合要求，请检查后重试"),
    CourseUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    ResumePhotoIsNullException("10001", "文件为空，请检查后重试"),
    SaveResumePhotoException("10001", "图片上传失败，请检查后重试"),
    CompanySoleCodeDuplicateException("10001", "统一社会信用码/工商注册码已存在"),
    CompanyInfoException("10001", "该公司信息有误，注册失败"),
    CompanyDeleteFailedException("10001", "删除公司记录失败，请检查所提交的数据内容"),
    ResumeDeleteFailedException("10001", "删除简历记录失败，请检查后重试"),
    ParamsIsNullException("10001", "请求参数为空，请检查后重试"),
    UpdateCompanyInfoException("10001","数据更新失败，请检查后重试"),
    SoleCodeInfoException("10001","数据更新失败，该公司用户信息不存在，请联系管理员"),
    OriginalPasswordErrorException("10001","密码更新失败，原密码错误，请检查后重试"),
    StudentNoInfoException("10001","数据更新失败，该学生用户信息不存在，请联系管理员"),
    OrganizationSaveException("10001", "数据保存失败，该机构名称已存在，请检查后重试"),
    ResumeTemplateIdsErrorException("10001", "提交的数据有误，删除失败，请检查后重试"),
    CompanySoleCodeErrorException("10001", "该企业用户信息不存在，请重新登录验证身份"),
    CompanyUploadResumeErrorException("10001", "当前账户没有权限下载，请前往会员缴费页面缴费成为会员后重试"),
    StudentCourseDetailUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    StudentInfoNotExistException("10001", "位于[第 %s 行]的学生信息不存在，请检查后重试"),
    CourseInfoNotExistException("10001", "位于[第 %s 行]的课程信息不存在，请检查后重试"),
    StudentDetailUploadDuplicateException("10001", "上传成功共有%s条记录，其中有 %s 记录已经存在, 故不允许再次添加"),
    UserNameNotExistException("10001", "该用户名不存在，请检查数据后重试"),
    UserNameDuplicateException("10001", "数据保存失败，该登录用户名已存在，请检查后重试");

    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
