/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.WallpaperLinkCacheBean;
import com.qinyin.athene.cache.store.Store;
import com.qinyin.athene.cache.store.WallpaperLinkStore;
import org.apache.commons.lang.StringUtils;

public class WallpaperLinkStoreImpl implements WallpaperLinkStore {
    private Store store;

    @Override
    public WallpaperLinkCacheBean get(String name) {
        if (StringUtils.isBlank(name)) return null;
        Object obj = store.get(PREFIX + name);
        return obj == null ? null : (WallpaperLinkCacheBean) obj;
    }

    @Override
    public void put(String name, WallpaperLinkCacheBean value) {
        if (StringUtils.isBlank(name) || value == null) return;
        store.put(PREFIX + name, value, MAX_AGE);
    }

    @Override
    public void remove(String name) {
        if (StringUtils.isBlank(name)) return;
        store.remove(PREFIX + name);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
