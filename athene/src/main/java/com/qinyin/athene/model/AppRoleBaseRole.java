package com.qinyin.athene.model;

public class AppRoleBaseRole extends AdvanceModel {
    public static final String BASE_ROLE_NAME = "baseRoleName";
    private String baseRoleName;
    public static final String ROLE_NAME = "roleName";
    private String roleName;

    public static final String SQL_FILE_NAME = "AppRoleBaseRole";

    public String getBaseRoleName() {
        return baseRoleName;
    }

    public void setBaseRoleName(String baseRoleName) {
        this.baseRoleName = baseRoleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}