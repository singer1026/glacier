package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.LessonPlan;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonPlanService extends BaseService<LessonPlan, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonPlan> queryListByLessonId(Integer lessonId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<LessonPlan> queryListByTeacherId(Integer teacherId);

    @Transactional(rollbackFor = Exception.class)
    public void deleteByLessonId(Integer lessonId);
}
