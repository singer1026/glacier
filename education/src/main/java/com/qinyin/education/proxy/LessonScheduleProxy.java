package com.qinyin.education.proxy;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface LessonScheduleProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryTeacherScheduleByTeacherId(Integer id);
}
