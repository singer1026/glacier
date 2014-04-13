package com.qinyin.customer.model;

import com.qinyin.athene.model.AdvanceModel;

import java.sql.Timestamp;

public class Task extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String STATUS = "status";
    private String status;
    public static final String NOTIFY_DATE = "notifyDate";
    private Timestamp notifyDate;
    public static final String TITLE = "title";
    private String title;
    public static final String DESCRIPTION = "description";
    private String description;
    public static final String DISPOSER = "disposer";
    private String disposer;
    public static final String DISPOSE_TIME = "disposeTime";
    private Timestamp disposeTime;
    public static final String DISPOSE_OPINION = "disposeOpinion";
    private String disposeOpinion;

    public static final String SQL_FILE_NAME = "CusTask";


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Timestamp notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposer() {
        return disposer;
    }

    public void setDisposer(String disposer) {
        this.disposer = disposer;
    }

    public Timestamp getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Timestamp disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getDisposeOpinion() {
        return disposeOpinion;
    }

    public void setDisposeOpinion(String disposeOpinion) {
        this.disposeOpinion = disposeOpinion;
    }
}