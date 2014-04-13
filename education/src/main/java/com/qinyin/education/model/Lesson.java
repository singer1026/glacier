package com.qinyin.education.model;

import com.qinyin.athene.model.AdvanceModel;

public class Lesson extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String SUBJECT_ID = "subjectId";
    private Integer subjectId;
    public static final String REMARK = "remark";
    private String remark;

    public static final String SQL_FILE_NAME = "EduLesson";

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}