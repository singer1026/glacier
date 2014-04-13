package com.qinyin.customer.proxy;

import com.qinyin.customer.model.Task;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskNotificationProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryTodayList();

    @Transactional(rollbackFor = Exception.class)
    public void saveByTask(Task task);

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTaskId(Integer taskId);

    @Transactional(rollbackFor = Exception.class)
    public void updateRead(Integer taskId);
}
