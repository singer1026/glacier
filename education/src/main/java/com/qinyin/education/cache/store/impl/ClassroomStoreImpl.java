/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.store.impl;

import com.qinyin.athene.cache.store.Store;
import com.qinyin.education.cache.model.ClassroomCacheBean;
import com.qinyin.education.cache.store.ClassroomStore;

import java.util.ArrayList;
import java.util.List;

public class ClassroomStoreImpl implements ClassroomStore {
    private Store store;

    @Override
    public ClassroomCacheBean get(Integer id) {
        if (id == null || id == 0) return null;
        Object obj = store.get(PREFIX + id);
        return obj == null ? null : (ClassroomCacheBean) obj;
    }

    @Override
    public void put(Integer id, ClassroomCacheBean value) {
        if (id == null || id == 0 || value == null) return;
        store.put(PREFIX + id, value, MAX_AGE);
    }

    @Override
    public void remove(Integer id) {
        if (id == null || id == 0) return;
        store.remove(PREFIX + id);
    }

    @Override
    public List getListByCompanyId(Integer companyId) {
        if (companyId == null || companyId == 0) return new ArrayList();
        return (List) store.get(PREFIX + companyId + POSTFIX);
    }

    @Override
    public void putList(Integer companyId, List list) {
        if (companyId == null || companyId == 0 || list == null || list.size() == 0) return;
        store.put(PREFIX + companyId + POSTFIX, list, MAX_AGE);
    }

    @Override
    public void removeByCompanyId(Integer companyId) {
        if (companyId == null || companyId == 0) return;
        store.remove(PREFIX + companyId + POSTFIX);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
