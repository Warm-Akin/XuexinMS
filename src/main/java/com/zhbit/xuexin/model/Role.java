package com.zhbit.xuexin.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_sys_role")
public class Role implements Serializable {

    @GenericGenerator(name = "generator", strategy = "uuid")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ROLE_ID", unique = true, nullable = false, length = 32)
    private String roleId;

    @Column(name = "ROLE_NAME", length = 64)
    private String roleName;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", length = 7)
    private Date createTime;

    @Column(name = "ROLE_NO", length = 32)
    private String roleNo;

    @Column(name = "MEMO", length = 200)
    private String memo;

    @Column(name = "PARENT_ORG_ID", length = 32)
    private String parentOrgId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

}
