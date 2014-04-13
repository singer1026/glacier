package com.qinyin.education.model;

import com.qinyin.athene.model.AdvanceModel;

import java.sql.Timestamp;

public class Student extends AdvanceModel {
    public static final String STATUS = "status";
    private String status;
    public static final String GMT_ENTER = "gmtEnter";
    private Timestamp gmtEnter;
    public static final String GMT_QUIT = "gmtQuit";
    private Timestamp gmtQuit;
    public static final String LOGIN_ID = "loginId";
    private String loginId;
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String SCHOOL = "school";
    private String school;
    public static final String DESCRIPTION = "description";
    private String description;

    public static final String SQL_FILE_NAME = "EduStudent";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getGmtEnter() {
        return gmtEnter;
    }

    public void setGmtEnter(Timestamp gmtEnter) {
        this.gmtEnter = gmtEnter;
    }

    public Timestamp getGmtQuit() {
        return gmtQuit;
    }

    public void setGmtQuit(Timestamp gmtQuit) {
        this.gmtQuit = gmtQuit;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}