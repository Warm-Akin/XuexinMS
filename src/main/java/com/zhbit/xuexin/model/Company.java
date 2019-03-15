package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "T_COMPANY")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company {

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    private String id;

    @Column(name = "SOLE_CODE", length = 200)
    private String soleCode;

    @Column(name = "COMPANY_NAME", length = 64)
    private String companyName;

    @Column(name = "LEGAL_NAME", length = 64)
    private String legalName;

    @Column(name = "LEGAL_PHONE", length = 64)
    private String legalPhone;

    @Column(name = "LEGAL_CERTCODE", length = 64)
    private String legalCertcode;

    @Column(name = "COMP_ADDRESS", length = 300)
    private String companyAddress;

    @Column(name = "COMP_PHONE", length = 64)
    private String companyPhone;

    @Column(name = "CREATE_BY", length = 64)
    private String createBy;

    @Column(name = "SYSOPR_STATE", length = 10, columnDefinition = "varchar(10) default 'OPEN'", nullable = false)
    private String sysoprState; // 公司状态

    @Column(name = "CREATE_DATE", length = 64)
    private String createDate;

    @Column(name = "UPDATE_DATE", length = 64)
    private String updateDate;

    @Column(name = "UPDATE_BY", length = 64)
    private String updateBy;

    @Column(name = "REMARKS", length = 255)
    private String remark;

    @Column(name = "DEL_FLAG", length = 2, columnDefinition = "varchar(2) DEFAULT '0'", nullable = false)
    private String deleteFlag;

    @Column(name = "PDF_LIMIT", length = 20, columnDefinition = "varchar(20) default '-1'", nullable = false)
    private String pdfLimit; // 可看简历数量限制

    @Column(name = "PASSWORD", length = 64)
    private String password;

    @Column(name = "PHOTOPATH",length = 255)
    private String photoPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoleCode() {
        return soleCode;
    }

    public void setSoleCode(String soleCode) {
        this.soleCode = soleCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }

    public String getLegalCertcode() {
        return legalCertcode;
    }

    public void setLegalCertcode(String legalCertcode) {
        this.legalCertcode = legalCertcode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getSysoprState() {
        return sysoprState;
    }

    public void setSysoprState(String sysoprState) {
        this.sysoprState = sysoprState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getPdfLimit() {
        return pdfLimit;
    }

    public void setPdfLimit(String pdfLimit) {
        this.pdfLimit = pdfLimit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}