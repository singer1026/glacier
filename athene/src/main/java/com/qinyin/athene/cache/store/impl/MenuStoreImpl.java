/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.MenuCacheBean;
import com.qinyin.athene.cache.store.MenuStore;
import com.qinyin.athene.cache.store.Store;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuStoreImpl implements MenuStore {
    private Store store;

    @Override
    public MenuCacheBean get(String name) {
        if (StringUtils.isBlank(name)) return null;
        Object obj = store.get(PREFIX + name);
        return obj == null ? null : (MenuCacheBean) obj;
    }

    @Override
    public void put(String name, MenuCacheBean value) {
        if (StringUtils.isBlank(name) || value == null) return;
        store.put(PREFIX + name, value, MAX_AGE);
    }

    @Override
    public void remove(String name) {
        if (StringUtils.isBlank(name)) return;
        store.remove(PREFIX + name);
    }

    @Override
    public List getAllListByType(String type) {
        if (StringUtils.isBlank(type)) return new ArrayList();
        return (List) store.get(PREFIX + type + POSTFIX);
    }

    @Override
    public void putAllList(String type, List list) {
        if (StringUtils.isBlank(type) || list == null || list.size() == 0) return;
        store.put(PREFIX + type + POSTFIX, list, MAX_AGE);
    }

    @Override
    public void removeAllListByType(String type) {
        store.remove(PREFIX + type + POSTFIX);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
