package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.MenuCacheBean;

import java.util.List;

public interface MenuStore extends BaseStore<MenuCacheBean, String> {
    public final String PREFIX = "AppMenu-";
    public final String POSTFIX = "-Id-List";
    public final long MAX_AGE = 0;

    public List getAllListByType(String type);

    public void putAllList(String type, List list);

    public void removeAllListByType(String type);
}
