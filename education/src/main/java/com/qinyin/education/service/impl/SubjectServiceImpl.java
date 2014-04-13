/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.education.model.Subject;
import com.qinyin.education.service.SubjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectServiceImpl implements SubjectService {
    private JdbcDao jdbcDao;

    @Override
    public Subject queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Subject) jdbcDao.queryForObject(Subject.SQL_FILE_NAME, "SELECT", condition, Subject.class);
    }

    @Override
    public List<Subject> queryListByCompanyId(Integer companyId) {
        List<Subject> rtnList = new ArrayList<Subject>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Subject.COMPANY_ID, companyId);
        List list = jdbcDao.queryForList(Subject.SQL_FILE_NAME, "SELECT", condition, Subject.class);
        for (Object obj : list) {
            rtnList.add((Subject) obj);
        }
        return rtnList;
    }

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public int save(Subject subject) {
        return 0;
    }

    @Override
    public void update(Subject subject) {
    }

    @Override
    public void delete(Subject subject) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
