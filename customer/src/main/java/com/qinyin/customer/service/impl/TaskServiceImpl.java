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
import com.qinyin.athene.model.BaseModel;
import com.qinyin.customer.model.Task;
import com.qinyin.customer.service.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskServiceImpl implements TaskService {
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Task.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Task.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(Task.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Task.SQL_FILE_NAME, "QUERY", paramMap, Task.class);
    }

    @Override
    public Task queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Task) jdbcDao.queryForObject(Task.SQL_FILE_NAME, "SELECT", condition, Task.class);
    }

    @Override
    public int save(Task task) {
        return jdbcDao.insert(Task.SQL_FILE_NAME, "INSERT", task);
    }

    @Override
    public void update(Task task) {
        if (jdbcDao.update(Task.SQL_FILE_NAME, "UPDATE", task) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Task task) {
        if (jdbcDao.update(Task.SQL_FILE_NAME, "DELETE", task) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void approve(Task task) {
        if (jdbcDao.update(Task.SQL_FILE_NAME, "APPROVE", task) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
