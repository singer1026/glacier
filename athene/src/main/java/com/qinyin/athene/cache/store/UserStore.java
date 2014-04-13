package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.UserCacheBean;

public interface UserStore extends BaseStore<UserCacheBean, String> {
    public final String PREFIX = "AppUser-";
    public final long MAX_AGE = 0;
}
