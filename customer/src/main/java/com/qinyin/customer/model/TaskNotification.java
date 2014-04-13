package com.qinyin.customer.model;

import com.qinyin.athene.model.BaseModel;

public class TaskNotification extends BaseModel {
    public static final String STATUS = "status";
    private String status;
    public static final String TASK_ID = "taskId";
    private Integer taskId;
    public static final String LOGIN_ID = "loginId";
    private String loginId;

    public static final String SQL_FILE_NAME = "CusTaskNotification";

    public static final String SAVE_ROLE = "base_today_task_notification_query";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}