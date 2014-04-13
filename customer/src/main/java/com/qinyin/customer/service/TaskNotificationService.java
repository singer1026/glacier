package com.qinyin.customer.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.customer.model.TaskNotification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TaskNotificationService extends BaseService<TaskNotification, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryListByNotifyDate(Date notifyDate);

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTaskId(Integer taskId);

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(TaskNotification taskNotification);
}
