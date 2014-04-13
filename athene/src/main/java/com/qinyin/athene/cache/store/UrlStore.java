package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.UrlCacheBean;

public interface UrlStore extends BaseStore<UrlCacheBean, String> {
    public final String PREFIX = "AppUrl-";
    public final long MAX_AGE = 0;
}
