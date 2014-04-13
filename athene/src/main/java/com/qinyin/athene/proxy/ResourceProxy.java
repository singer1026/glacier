package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ResourceProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public ResourceCacheBean getCommonBean(String category);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public ResourceCacheBean getCompanyBean(String category);
}
