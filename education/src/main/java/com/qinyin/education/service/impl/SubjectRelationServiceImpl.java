/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.service.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.education.model.SubjectRelation;
import com.qinyin.education.service.SubjectRelationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectRelationServiceImpl implements SubjectRelationService {
    private JdbcDao jdbcDao;

    @Override
    public SubjectRelation queryById(Integer id) {
        return null;
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
    public List<SubjectRelation> queryByTeacherId(Integer teacherId) {
        List<SubjectRelation> rtnList = new ArrayList<SubjectRelation>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(SubjectRelation.OBJECT_ID, teacherId);
        condition.put(SubjectRelation.OBJECT_TYPE, ModelConstants.SUBJECT_RELATION_TYPE_TEACHER);
        List<Object> list = jdbcDao.queryForList(SubjectRelation.SQL_FILE_NAME, "SELECT", condition, SubjectRelation.class);
        for (Object obj : list) {
            rtnList.add((SubjectRelation) obj);
        }
        return rtnList;
    }

    @Override
    public List<SubjectRelation> queryByStudentId(Integer studentId) {
        List<SubjectRelation> rtnList = new ArrayList<SubjectRelation>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(SubjectRelation.OBJECT_ID, studentId);
        condition.put(SubjectRelation.OBJECT_TYPE, ModelConstants.SUBJECT_RELATION_TYPE_STUDENT);
        List<Object> list = jdbcDao.queryForList(SubjectRelation.SQL_FILE_NAME, "SELECT", condition, SubjectRelation.class);
        for (Object obj : list) {
            rtnList.add((SubjectRelation) obj);
        }
        return rtnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SubjectRelation subjectRelation) {
        return jdbcDao.insert(SubjectRelation.SQL_FILE_NAME, "INSERT", subjectRelation);
    }

    @Override
    public void update(SubjectRelation subjectRelation) {
    }

    @Override
    public void delete(SubjectRelation subjectRelation) {
        if (jdbcDao.update(SubjectRelation.SQL_FILE_NAME, "DELETE", subjectRelation) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
