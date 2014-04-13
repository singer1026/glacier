package com.qinyin.education.model;

import com.qinyin.athene.model.AdvanceModel;

public class Teacher extends AdvanceModel {
    public static final String STATUS = "status";
    private String status;
    public static final String LOGIN_ID = "loginId";
    private String loginId;
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String GRADUATE_SCHOOL = "graduateSchool";
    private String graduateSchool;
    public static final String EDUCATION = "education";
    private String education;
    public static final String LEVEL = "level";
    private String level;
    public static final String MAJOR = "major";
    private String major;
    public static final String EXPERIENCE = "experience";
    private String experience;
    public static final String DESCRIPTION = "description";
    private String description;

    public static final String SQL_FILE_NAME = "EduTeacher";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}