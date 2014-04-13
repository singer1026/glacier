package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.UrlCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UrlProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public UrlCacheBean queryByName(String name);
}
