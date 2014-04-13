package com.qinyin.education.proxy;

import com.qinyin.education.cache.model.LessonCacheBean;
import com.qinyin.education.wrap.LessonWrap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface LessonProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public LessonCacheBean queryById(Integer id);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public LessonWrap queryForWrap(Integer id);

    @Transactional(rollbackFor = Exception.class)
    public void save();

    @Transactional(rollbackFor = Exception.class)
    public void update();

    @Transactional(rollbackFor = Exception.class)
    public void delete();

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryLessonStatistics();

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Map<String, Object> queryLessonTotalStatistics(List<Object> sourceList);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryLessonDayStatistics(List<Object> sourceList);
}

