/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.customer.model.Task;
import com.qinyin.customer.model.TaskNotification;
import com.qinyin.customer.service.TaskNotificationService;

import java.util.*;


public class TaskNotificationServiceImpl implements TaskNotificationService {
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public TaskNotification queryById(Integer id) {
        return null;
    }

    @Override
    public int save(TaskNotification taskNotification) {
        return jdbcDao.insert(TaskNotification.SQL_FILE_NAME, "INSERT", taskNotification);
    }

    @Override
    public void update(TaskNotification taskNotification) {
        if (jdbcDao.update(TaskNotification.SQL_FILE_NAME, "UPDATE", taskNotification) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(TaskNotification taskNotification) {
        if (jdbcDao.update(TaskNotification.SQL_FILE_NAME, "DELETE", taskNotification) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public List<Object> queryListByNotifyDate(Date notifyDate) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(TaskNotification.LOGIN_ID, pvgInfo.getLoginId());
        condition.put(Task.NOTIFY_DATE, notifyDate);
        List<String> ordering = new ArrayList<String>();
        ordering.add("tn.status DESC");
        ordering.add("t.notify_date DESC");
        condition.put("ordering", ordering);
        return jdbcDao.queryForList(TaskNotification.SQL_FILE_NAME, "SELECT_BY_NOTIFY_DATE", condition);
    }

    @Override
    public void deleteByTaskId(Integer taskId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(TaskNotification.TASK_ID, taskId);
        jdbcDao.update(TaskNotification.SQL_FILE_NAME, "DELETE", condition);
    }

    @Override
    public void updateStatus(TaskNotification taskNotification) {
        jdbcDao.update(TaskNotification.SQL_FILE_NAME, "UPDATE_STATUS", taskNotification);
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
