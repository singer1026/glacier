package com.qinyin.education.model;

import com.qinyin.athene.model.AdvanceModel;

public class Subject extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String NAME = "name";
    private String name;

    public static final String SQL_FILE_NAME = "EduSubject";

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
}