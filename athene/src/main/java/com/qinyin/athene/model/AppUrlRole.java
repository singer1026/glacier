package com.qinyin.athene.model;

public class AppUrlRole extends AdvanceModel {
    public static final String URL_NAME = "urlName";
    private String urlName;
    public static final String ROLE_NAME = "roleName";
    private String roleName;

    public static final String SQL_FILE_NAME = "AppUrlRole";

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}