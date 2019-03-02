package com.zhbit.xuexin.common.response;

import com.zhbit.xuexin.common.constant.HttpCode;

public enum ResultEnum {

    UnknownException(HttpCode.ERROR, "服务器异常！请重试或联系管理员"),
    AccountInvalidException("10001", "用户名或密码错误，请重试"),
    StudentNoDuplicatedException("10001", "学号信息重复，保存失败"),
    FileIsNullException("10001", "文件为空，请重试"),
    StudentUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    TeacherUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试"),
    TeacherNoDuplicatedException("10001", "教师工号信息重复，保存失败"),
    CourseCodeDuplicatedException("10001", "学号信息重复，保存失败"),
    CourseInfoError("10001", "位于[第 %s 行]的限选总人数和选课人数不符合要求，请检查后重试"),
    CourseUploadIncomplete("10001", "位于[第 %s 行]的必填列信息不符合要求，请检查后重试");
//    DBDeleteCorrelationException("02001", "Please noted that %s are reference by others, you can't delete them."),
//    MailFailException("02001", "Please noted that send mail to below vendor(s) failed %s"),
//    VendorSaveDuplicatedException("02001", "Duplicated vendor code record!"),
//    VendorUploadIncomplete("02001", "Some of the content[row at %s] is invalid thus cannot be parsed. Please check and upload it again."),
//    FileIsNullException("02001", "The file is null and please choose again."),
//    WorkBookIsNullException("02001", "The content is null and please check and upload it again."),
//    StakeholderSaveDuplicatedException("02001", "Duplicated Organization and Email record!"),
//    StakeholderUploadIncomplete("02001", "Some of the content[row at %s] is invalid thus cannot be parsed. Please check and upload it again."),
//    CategoryCodeNotFoundException("02001", "The following categories are not found: %s. Please add it first."),
//    CategoryCodeDuplicatedException("02001", "The Category Code [%s] is duplicated. Please check and try again."),
//    AssessmentInvalidException("02001", "The excel file of [%s] content is not complete. Please check and upload it again."),
//    AssessmentCannotFindException("02001", "The [%s] can not find. Please contact the administrator."),
//    AssessmentSheetCountException("02001", "The count of sheet is invalid. Please check and upload it again."),
//    AssessmentRowCountException("02001", "The count of row is invalid. Please check and upload it again."),
//    MailTemplateException(HttpCode.ERROR, "Email template not found, Please check ESG_EMAIL_DELIVER_CONFIG data in DB"),
//    CategoryAndDictKeyDuplicatedException("02001", "Duplicated record found, please modify Category or DictKey, and try it again."),
//    EmailTypeDuplicatedException("02001",  "Duplicated record found, please modify Type, and try it again."),
//    AdminUserDuplicatedException("02001", "Duplicated record found, please modify DomainId or Type, and try it again."),
//    DBDeleteException("02001", "Delete failed. Please try again.");

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
