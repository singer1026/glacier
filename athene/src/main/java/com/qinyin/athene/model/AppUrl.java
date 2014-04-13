package com.qinyin.athene.model;

public class AppUrl extends AdvanceModel {
    public static final String IS_ANONYMOUS = "isAnonymous";
    private String isAnonymous;
    public static final String HAS_PERMISSION = "hasPermission";
    private String hasPermission;
    public static final String NAME = "name";
    private String name;
    public static final String TYPE = "type";
    private String type;
    public static final String DESCRIPTION = "description";
    private String description;

    public static final String SQL_FILE_NAME = "AppUrl";

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(String hasPermission) {
        this.hasPermission = hasPermission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}