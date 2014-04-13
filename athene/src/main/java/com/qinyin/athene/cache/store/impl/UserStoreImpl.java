/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.cache.store.Store;
import com.qinyin.athene.cache.store.UserStore;
import org.apache.commons.lang.StringUtils;

public class UserStoreImpl implements UserStore {
    private Store store;

    @Override
    public UserCacheBean get(String loginId) {
        if (StringUtils.isBlank(loginId)) return null;
        Object obj = store.get(PREFIX + loginId);
        return obj == null ? null : (UserCacheBean) obj;
    }

    @Override
    public void put(String loginId, UserCacheBean value) {
        if (StringUtils.isBlank(loginId) || value == null) return;
        store.put(PREFIX + loginId, value, MAX_AGE);
    }

    @Override
    public void remove(String loginId) {
        if (StringUtils.isBlank(loginId)) return;
        store.remove(PREFIX + loginId);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
