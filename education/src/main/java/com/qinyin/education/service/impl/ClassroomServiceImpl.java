/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.education.model.Classroom;
import com.qinyin.education.service.ClassroomService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassroomServiceImpl implements ClassroomService {
    private JdbcDao jdbcDao;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Classroom queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Classroom) jdbcDao.queryForObject(Classroom.SQL_FILE_NAME, "SELECT", condition, Classroom.class);
    }

    @Override
    public List<Classroom> queryListByCompanyId(Integer companyId) {
        List<Classroom> rtnList = new ArrayList<Classroom>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Classroom.COMPANY_ID, companyId);
        List list = jdbcDao.queryForList(Classroom.SQL_FILE_NAME, "SELECT", condition, Classroom.class);
        for (Object obj : list) {
            rtnList.add((Classroom) obj);
        }
        return rtnList;
    }

    @Override
    public int save(Classroom classroom) {
        return 0;
    }

    @Override
    public void update(Classroom classroom) {
    }

    @Override
    public void delete(Classroom classroom) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
