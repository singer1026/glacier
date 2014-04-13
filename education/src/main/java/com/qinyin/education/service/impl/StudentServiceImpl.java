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
import com.qinyin.education.model.Student;
import com.qinyin.education.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentServiceImpl implements StudentService {
    public static Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Student.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Student.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put("companyId", pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Student.SQL_FILE_NAME, "QUERY", paramMap, Student.class);
    }

    @Override
    public Student queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Student) jdbcDao.queryForObject(Student.SQL_FILE_NAME, "SELECT", condition, Student.class);
    }

    @Override
    public int save(Student student) {
        return jdbcDao.insert(Student.SQL_FILE_NAME, "INSERT", student);
    }

    @Override
    public void update(Student student) {
        if (jdbcDao.update(Student.SQL_FILE_NAME, "UPDATE", student) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Student student) {
        if (jdbcDao.update(Student.SQL_FILE_NAME, "DELETE", student) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void updateStatus(Student student) {
        if (jdbcDao.update(Student.SQL_FILE_NAME, "UPDATE_STATUS", student) == 0) {
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
