package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.RoleCacheBean;

public interface RoleStore extends BaseStore<RoleCacheBean, String> {
    public final String PREFIX = "AppRole-";
    public final long MAX_AGE = 0;
}
