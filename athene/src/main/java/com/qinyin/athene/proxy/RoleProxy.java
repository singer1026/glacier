package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.RoleCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RoleProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public RoleCacheBean queryByName(String roleName);
}
