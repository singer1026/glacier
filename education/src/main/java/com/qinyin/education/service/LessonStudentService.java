package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.LessonStudent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonStudentService extends BaseService<LessonStudent, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonStudent> queryListByLessonId(Integer lessonId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonStudent> queryListByStudentId(Integer studentId);

    @Transactional(rollbackFor = Exception.class)
    public void deleteByLessonId(Integer lessonId);

    @Transactional(rollbackFor = Exception.class)
    public void updateStudentName(Integer studentId, String studnetName);
}
