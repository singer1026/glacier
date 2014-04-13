package com.qinyin.athene.model;

public class AppRole extends AdvanceModel {
    public static final String TYPE = "type";
    private String type;
    public static final String NAME = "name";
    private String name;
    public static final String DESCRIPTION = "description";
    private String description;

    public static final String SQL_FILE_NAME = "AppRole";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}