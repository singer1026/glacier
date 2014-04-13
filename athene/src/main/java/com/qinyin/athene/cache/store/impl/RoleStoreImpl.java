/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.RoleCacheBean;
import com.qinyin.athene.cache.store.RoleStore;
import com.qinyin.athene.cache.store.Store;
import org.apache.commons.lang.StringUtils;

public class RoleStoreImpl implements RoleStore {
    private Store store;

    @Override
    public RoleCacheBean get(String name) {
        if (StringUtils.isBlank(name)) return null;
        Object obj = store.get(PREFIX + name);
        return obj == null ? null : (RoleCacheBean) obj;
    }

    @Override
    public void put(String name, RoleCacheBean value) {
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
