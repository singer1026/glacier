/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.education.model.LessonTeacher;
import com.qinyin.education.service.LessonTeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonTeacherServiceImpl implements LessonTeacherService {
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
    public LessonTeacher queryById(Integer id) {
        return null;
    }

    @Override
    public List<LessonTeacher> queryListByLessonId(Integer lessonId) {
        List<LessonTeacher> rtnList = new ArrayList<LessonTeacher>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonTeacher.LESSON_ID, lessonId);
        List<Object> list = jdbcDao.queryForList(LessonTeacher.SQL_FILE_NAME, "SELECT", condition, LessonTeacher.class);
        for (Object obj : list) {
            rtnList.add((LessonTeacher) obj);
        }
        return rtnList;
    }

    @Override
    public List<LessonTeacher> queryListByTeacherId(Integer teacherId) {
        List<LessonTeacher> rtnList = new ArrayList<LessonTeacher>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonTeacher.TEACHER_ID, teacherId);
        List<Object> list = jdbcDao.queryForList(LessonTeacher.SQL_FILE_NAME, "SELECT", condition, LessonTeacher.class);
        for (Object obj : list) {
            rtnList.add((LessonTeacher) obj);
        }
        return rtnList;
    }

    @Override
    public int save(LessonTeacher lessonTeacher) {
        return jdbcDao.insert(LessonTeacher.SQL_FILE_NAME, "INSERT", lessonTeacher);
    }

    @Override
    public void update(LessonTeacher lessonTeacher) {
    }

    @Override
    public void updateTeacherName(Integer teacherId, String teacherName) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonTeacher.TEACHER_ID, teacherId);
        condition.put(LessonTeacher.TEACHER_NAME, teacherName);
        jdbcDao.update(LessonTeacher.SQL_FILE_NAME, "UPDATE_TEACHER_NAME", condition);
    }

    @Override
    public void delete(LessonTeacher lessonTeacher) {
        if (jdbcDao.update(LessonTeacher.SQL_FILE_NAME, "DELETE", lessonTeacher) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void deleteByLessonId(Integer lessonId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonTeacher.LESSON_ID, lessonId);
        jdbcDao.update(LessonTeacher.SQL_FILE_NAME, "DELETE_BY_LESSON_ID", condition);
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
