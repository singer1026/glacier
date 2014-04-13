package com.qinyin.education.proxy;

import com.qinyin.education.cache.model.TeacherCacheBean;
import com.qinyin.education.wrap.TeacherWrap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TeacherProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public TeacherWrap queryForWrap(Integer id);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public TeacherCacheBean queryById(Integer id);

    @Transactional(rollbackFor = Exception.class)
    public void save();

    @Transactional(rollbackFor = Exception.class)
    public void update();

    @Transactional(rollbackFor = Exception.class)
    public void delete();

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String status);
}