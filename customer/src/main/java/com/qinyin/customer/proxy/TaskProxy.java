package com.qinyin.customer.proxy;

import com.qinyin.customer.wrap.TaskWrap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TaskProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public TaskWrap queryForWrap(Integer id);


    @Transactional(rollbackFor = Exception.class)
    public void save();

    @Transactional(rollbackFor = Exception.class)
    public void update();

    @Transactional(rollbackFor = Exception.class)
    public void delete();

    @Transactional(rollbackFor = Exception.class)
    public void approve();

}
