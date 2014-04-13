/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.store.impl;

import com.qinyin.athene.cache.store.Store;
import com.qinyin.education.cache.model.LessonCacheBean;
import com.qinyin.education.cache.store.LessonStore;

public class LessonStoreImpl implements LessonStore {
    private Store store;

    @Override
    public LessonCacheBean get(Integer id) {
        if (id == null || id == 0) return null;
        Object obj = store.get(PREFIX + id);
        return obj == null ? null : (LessonCacheBean) obj;
    }

    @Override
    public void put(Integer id, LessonCacheBean value) {
        if (id == null || id == 0 || value == null) return;
        store.put(PREFIX + id, value, MAX_AGE);
    }

    @Override
    public void remove(Integer id) {
        if (id == null || id == 0) return;
        store.remove(PREFIX + id);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
