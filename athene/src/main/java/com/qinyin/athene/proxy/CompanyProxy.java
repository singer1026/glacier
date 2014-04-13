package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.CompanyCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public CompanyCacheBean queryById(Integer id);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<CompanyCacheBean> queryAllList();
}
