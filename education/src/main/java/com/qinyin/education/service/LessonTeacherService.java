package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.LessonTeacher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonTeacherService extends BaseService<LessonTeacher, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonTeacher> queryListByLessonId(Integer lessonId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonTeacher> queryListByTeacherId(Integer teacherId);

    @Transactional(rollbackFor = Exception.class)
    public void deleteByLessonId(Integer lessonId);

    @Transactional(rollbackFor = Exception.class)
    public void updateTeacherName(Integer teacherId, String teacherName);
}
