/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-3-27
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.education.model.Lesson;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.service.LessonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonServiceImpl implements LessonService {
    private PrivilegeInfo pvgInfo;
    private JdbcDao jdbcDao;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Lesson.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Lesson.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(Lesson.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Lesson.SQL_FILE_NAME, "QUERY", paramMap, Lesson.class);
    }

    @Override
    public int save(Lesson lessson) {
        return jdbcDao.insert(Lesson.SQL_FILE_NAME, "INSERT", lessson);
    }

    @Override
    public Lesson queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Lesson) jdbcDao.queryForObject(Lesson.SQL_FILE_NAME, "SELECT", condition, Lesson.class);
    }

    public List<Object> queryLessonStatisticsByCompanyId(Integer companyId) {
        List<Object> rtnList = new ArrayList<Object>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Lesson.COMPANY_ID, companyId);
        for (int i = 0; i < 7; i++) {
            condition.put(LessonPlan.DAY_OF_WEEK, i);
            rtnList.add(jdbcDao.queryForList(Lesson.SQL_FILE_NAME, "SELECT_DAY_STATISTICS", condition));
        }
        return rtnList;
    }

    @Override
    public void update(Lesson lessson) {
        if (jdbcDao.update(Lesson.SQL_FILE_NAME, "UPDATE", lessson) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Lesson lessson) {
        if (jdbcDao.update(Lesson.SQL_FILE_NAME, "DELETE", lessson) == 0) {
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
