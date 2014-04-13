/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.store.impl;

import com.qinyin.athene.cache.store.Store;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.cache.store.SubjectStore;

import java.util.List;

public class SubjectStoreImpl implements SubjectStore {
    private Store store;

    @Override
    public SubjectCacheBean get(Integer id) {
        if (id == null || id == 0) return null;
        Object obj = store.get(PREFIX + id);
        return obj == null ? null : (SubjectCacheBean) obj;
    }

    @Override
    public void put(Integer id, SubjectCacheBean value) {
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
        if (companyId == null || companyId == 0) return null;
        return (List) store.get(PREFIX + companyId + POSTFIX);
    }

    @Override
    public void putList(Integer companyId, List list) {
        if (companyId == null || companyId == 0 || list == null || list.size() == 0) return;
        store.put(PREFIX + companyId + POSTFIX, list, MAX_AGE);
    }

    @Override
    public void removeListByCompanyId(Integer companyId) {
        if (companyId == null || companyId == 0) return;
        store.remove(PREFIX + companyId + POSTFIX);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
