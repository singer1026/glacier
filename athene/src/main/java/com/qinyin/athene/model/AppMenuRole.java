package com.qinyin.athene.model;

public class AppMenuRole extends AdvanceModel {
    public static final String MENU_NAME = "menuName";
    private String menuName;
    public static final String ROLE_NAME = "roleName";
    private String roleName;

    public static final String SQL_FILE_NAME = "AppMenuRole";

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}