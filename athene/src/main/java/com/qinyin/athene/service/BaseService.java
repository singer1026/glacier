/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseService<T, PK extends Serializable> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public T queryById(PK id);

    @Transactional(rollbackFor = Exception.class)
    public int save(T t);

    @Transactional(rollbackFor = Exception.class)
    public void update(T t);

    @Transactional(rollbackFor = Exception.class)
    public void delete(T t);
}
