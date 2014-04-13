package com.qinyin.athene.model;

public class AppOrg extends AdvanceModel {
    public static final String NAME = "name";
    private String name;
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String PARENT_ID = "parentId";
    private Integer parentId;
    public static final String FULL_ID_PATH = "fullIdPath";
    private String fullIdPath;
    public static final String FULL_NAME_PATH = "fullNamePath";
    private String fullNamePath;
    public static final String LEVEL = "level";
    private Integer level;

    public static final String SQL_FILE_NAME = "AppOrg";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getFullIdPath() {
        return fullIdPath;
    }

    public void setFullIdPath(String fullIdPath) {
        this.fullIdPath = fullIdPath;
    }

    public String getFullNamePath() {
        return fullNamePath;
    }

    public void setFullNamePath(String fullNamePath) {
        this.fullNamePath = fullNamePath;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}