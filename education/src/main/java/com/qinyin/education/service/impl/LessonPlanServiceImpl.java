/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.model.LessonTeacher;
import com.qinyin.education.service.LessonPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonPlanServiceImpl implements LessonPlanService {
    public static Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);
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
    public LessonPlan queryById(Integer id) {
        return null;
    }

    @Override
    public List<LessonPlan> queryListByLessonId(Integer lessonId) {
        List<LessonPlan> rtnList = new ArrayList<LessonPlan>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonPlan.LESSON_ID, lessonId);
        List<Object> list = jdbcDao.queryForList(LessonPlan.SQL_FILE_NAME, "SELECT", condition, LessonPlan.class);
        for (Object obj : list) {
            rtnList.add((LessonPlan) obj);
        }
        return rtnList;
    }

    @Override
    public List<LessonPlan> queryListByTeacherId(Integer teacherId) {
        List<LessonPlan> rtnList = new ArrayList<LessonPlan>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonTeacher.TEACHER_ID, teacherId);
        List<Object> list = jdbcDao.queryForList(LessonPlan.SQL_FILE_NAME, "SELECT_BY_TEACHER_ID", condition, LessonPlan.class);
        for (Object obj : list) {
            rtnList.add((LessonPlan) obj);
        }
        return rtnList;
    }

    @Override
    public int save(LessonPlan lessonPlan) {
        return jdbcDao.insert(LessonPlan.SQL_FILE_NAME, "INSERT", lessonPlan);
    }

    @Override
    public void update(LessonPlan lessonPlan) {
    }

    @Override
    public void delete(LessonPlan lessonPlan) {
        if (jdbcDao.update(LessonPlan.SQL_FILE_NAME, "DELETE", lessonPlan) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void deleteByLessonId(Integer lessonId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonPlan.LESSON_ID, lessonId);
        jdbcDao.update(LessonPlan.SQL_FILE_NAME, "DELETE_BY_LESSON_ID", condition);
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
