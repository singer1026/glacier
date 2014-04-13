package com.qinyin.athene.model;

public class AppUserRoleOrg extends AdvanceModel {
    public static final String LOGIN_ID = "loginId";
    private String loginId;
    public static final String ROLE_NAME = "roleName";
    private String roleName;
    public static final String ORG_ID = "orgId";
    private Integer orgId;

    public static final String SQL_FILE_NAME = "AppUserRoleOrg";

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}