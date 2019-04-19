package com.zhbit.xuexin.common.response;

import com.zhbit.xuexin.common.constant.HttpCode;

public enum ResultEnum {

    UnknownException(HttpCode.ERROR, "服务器异常！请重试或联系管理员"),
    EntityIsNullException("10001", "信息为空，保存失败，请检查后重试"),
    AccountInvalidException("10001", "用户名或密码错误，请检查后重试"),
    StudentNoDuplicatedException("10001", "学号信息重复，保存失败"),
    FileIsNullException("10001", "文件为空，请重试"),
    StudentUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    TeacherUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    TeacherNoDuplicatedException("10001", "教师工号信息重复，保存失败"),
    CourseCodeDuplicatedException("10001", "学号信息重复，保存失败"),
    CourseInfoError("10001", "位于[第 %s 行]的限选总人数和选课人数不符合要求，请检查后重试"),
    CourseUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    ResumePhotoIsNullException("10001", "文件为空，请检查后重试"),
    SaveResumePhotoException("10001", "图片上传失败，请检查后重试"),
    CompanySoleCodeDuplicateException("10001", "统一社会信用码/工商注册码已存在"),
    CompanyInfoException("10001", "该公司信息有误，注册失败"),
    CompanyDeleteFailedException("10001", "删除公司记录失败，请检查后重试"),
    ResumeDeleteFailedException("10001", "删除简历记录失败，请检查后重试"),
    ParamsIsNullException("10001", "请求参数为空，请检查后重试"),
    UpdateCompanyInfoException("10001","数据更新失败，请检查后重试"),
    SoleCodeInfoException("10001","数据更新失败，该公司用户信息不存在，请联系管理员"),
    OriginalPasswordErrorException("10001","密码更新失败，原密码错误，请检查后重试"),
    StudentNoInfoException("10001","数据更新失败，该学生用户信息不存在，请联系管理员");

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
