package com.qinyin.education.proxy;

import com.qinyin.education.cache.model.StudentCacheBean;
import com.qinyin.education.wrap.StudentWrap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface StudentProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public StudentWrap queryForWrap(Integer id);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public StudentCacheBean queryById(Integer id);

    @Transactional(rollbackFor = Exception.class)
    public void save();

    @Transactional(rollbackFor = Exception.class)
    public void update();

    @Transactional(rollbackFor = Exception.class)
    public void delete();

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String status);
}