package com.zhbit.xuexin.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "tb_sys_authority")
public class Authority {

    @GenericGenerator(name = "generator", strategy = "uuid")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "AUTHORITY_ID", unique = true, nullable = false, length = 32)
    private String authorityId;

    @Column(name = "PARENT_ID", length = 32)
    private String parentId;

    @Column(name = "PARENT_IDS", length = 1000)
    private String parentIds;

    @Column(name = "AUTHORITY_NAME", nullable = false, length = 64)
    private String authorityName;

    @Column(name = "AUTHORITY_TYPE", nullable = false, precision = 22, scale = 0)
    private Integer authorityType;

    @Column(name = "MODULE_NAME", length = 64)
    private String moduleName;

    @Column(name = "URL", length = 200)
    private String url;

    @Column(name = "OPERATION", length = 32)
    private String operation;

    @Column(name = "MENU_NO", length = 32)
    private String menuNo;

    @Column(name = "MEMO", length = 200)
    private String memo;

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public Integer getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType) {
        this.authorityType = authorityType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
