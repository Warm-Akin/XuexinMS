package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tb_sys_organization")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Organization implements Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "ORG_ID", length = 32)
    private String orgId;
    @Column(name = "ORG_NAME", length = 128)
    private String orgName;
    @Column(name = "PARENT_ID")
    private String parentId;
    @Column(name = "PARENT_IDS")
    private String parentIds;
    @Column(name = "ADDRESS", length = 128)
    private String address;
    @Column(name = "POST_CODE", length = 16)
    private String postCode;
    @Column(name = "CONTACT_MAN", length = 32)
    private String contactMan;
    @Column(name = "TELL", length = 16)
    private String telephone;
    @Column(name = "FAX", length = 16)
    private String fax;
    @Column(name = "EMAIL", length = 32)
    private String email;
    @Column(name = "STATUS", length = 16)
    private String status;
    @Column(name = "SCHOOL_FLAG", length = 2)
    private String schoolFlag;
    @Column(name = "SCHOOL_CODE", length = 20)
    private String schoolCode;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> userList;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getContactMan() {
        return contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchoolFlag() {
        return schoolFlag;
    }

    public void setSchoolFlag(String schoolFlag) {
        this.schoolFlag = schoolFlag;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
