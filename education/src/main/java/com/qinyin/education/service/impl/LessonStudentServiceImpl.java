/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.service.LessonStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonStudentServiceImpl implements LessonStudentService {
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
    public LessonStudent queryById(Integer id) {
        return null;
    }

    @Override
    public List<LessonStudent> queryListByLessonId(Integer lessonId) {
        List<LessonStudent> rtnList = new ArrayList<LessonStudent>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonStudent.LESSON_ID, lessonId);
        List<Object> list = jdbcDao.queryForList(LessonStudent.SQL_FILE_NAME, "SELECT", condition, LessonStudent.class);
        for (Object obj : list) {
            rtnList.add((LessonStudent) obj);
        }
        return rtnList;
    }

    @Override
    public List<LessonStudent> queryListByStudentId(Integer studentId) {
        List<LessonStudent> rtnList = new ArrayList<LessonStudent>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonStudent.STUDENT_ID, studentId);
        List<Object> list = jdbcDao.queryForList(LessonStudent.SQL_FILE_NAME, "SELECT", condition, LessonStudent.class);
        for (Object obj : list) {
            rtnList.add((LessonStudent) obj);
        }
        return rtnList;
    }

    @Override
    public void deleteByLessonId(Integer lessonId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonStudent.LESSON_ID, lessonId);
        jdbcDao.update(LessonStudent.SQL_FILE_NAME, "DELETE_BY_LESSON_ID", condition);
    }

    @Override
    public int save(LessonStudent lessonStudent) {
        return jdbcDao.insert(LessonStudent.SQL_FILE_NAME, "INSERT", lessonStudent);
    }

    @Override
    public void update(LessonStudent lessonStudent) {
    }

    @Override
    public void updateStudentName(Integer studentId, String studnetName) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(LessonStudent.STUDENT_ID, studentId);
        condition.put(LessonStudent.STUDENT_NAME, studnetName);
        jdbcDao.update(LessonStudent.SQL_FILE_NAME, "UPDATE_STUDENT_NAME", condition);
    }

    @Override
    public void delete(LessonStudent lessonStudent) {
        if (jdbcDao.update(LessonStudent.SQL_FILE_NAME, "DELETE", lessonStudent) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
