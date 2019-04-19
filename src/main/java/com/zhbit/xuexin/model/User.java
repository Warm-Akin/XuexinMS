package com.zhbit.xuexin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_sys_user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "USER_ID", length = 32)
    private String userId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ORG_ID")
    private Organization organization;

    @Column(name = "EMPLOY_NO", length = 16)
    private String employNo;

    @Column(name = "EMPLOY_NAME", length = 64)
    private String employName;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "TELL", length = 20)
    private String telephone;

    @Column(name = "STATUS")
    private double status; // 0 -> 启用, 1 -> 禁用

    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "PASSWORD", length = 50)
    private String password;

    @Column(name = "USER_TYPE", length = 2)
    private String userType; // 0 -> Teacher, 1 -> Student, 2 -> Admin, 3 -> Company

    @Column(name = "PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    @Column(name = "SEX", length = 2)
    private String sex;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getEmployNo() {
        return employNo;
    }

    public void setEmployNo(String employNo) {
        this.employNo = employNo;
    }

    public String getEmployName() {
        return employName;
    }

    public void setEmployName(String employName) {
        this.employName = employName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}