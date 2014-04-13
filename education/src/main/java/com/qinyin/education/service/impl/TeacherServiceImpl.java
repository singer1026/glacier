/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-1-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.education.model.Teacher;
import com.qinyin.education.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherServiceImpl implements TeacherService {
    public static Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Teacher.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Teacher.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(Teacher.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Teacher.SQL_FILE_NAME, "QUERY", paramMap, Teacher.class);
    }

    @Override
    public Teacher queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Teacher) jdbcDao.queryForObject(Teacher.SQL_FILE_NAME, "SELECT", condition, Teacher.class);
    }

    @Override
    public int save(Teacher teacher) {
        return jdbcDao.insert(Teacher.SQL_FILE_NAME, "INSERT", teacher);
    }

    @Override
    public void update(Teacher teacher) {
        if (jdbcDao.update(Teacher.SQL_FILE_NAME, "UPDATE", teacher) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Teacher teacher) {
        if (jdbcDao.update(Teacher.SQL_FILE_NAME, "DELETE", teacher) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void updateStatus(Teacher teacher) {
        if (jdbcDao.update(Teacher.SQL_FILE_NAME, "UPDATE_STATUS", teacher) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}