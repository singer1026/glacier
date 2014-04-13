package com.qinyin.customer.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.customer.model.Task;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService extends BaseService<Task, Integer> {
    @Transactional(rollbackFor = Exception.class)
    public void approve(Task task);
}
