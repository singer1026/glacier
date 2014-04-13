package com.qinyin.athene.model;

public class AppTag extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String NAME = "name";
    private String name;
    public static final String TYPE = "type";
    private String type;
    public static final String CATEGORY = "category";
    private String category;
    public static final String DESCRIPTION = "description";
    private String description;
    public static final String ORDERING = "ordering";
    private Integer ordering;

    public static final String SQL_FILE_NAME = "AppTag";

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
}