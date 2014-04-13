/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.CompanyCacheBean;
import com.qinyin.athene.cache.store.CompanyStore;
import com.qinyin.athene.cache.store.Store;

import java.util.List;

public class CompanyStoreImpl implements CompanyStore {
    private Store store;

    @Override
    public CompanyCacheBean get(Integer id) {
        if (id == null || id == 0) return null;
        Object obj = store.get(PREFIX + id);
        return obj == null ? null : (CompanyCacheBean) obj;
    }

    @Override
    public void put(Integer id, CompanyCacheBean value) {
        if (id == null || id == 0 || value == null) return;
        store.put(PREFIX + id, value, MAX_AGE);
    }

    @Override
    public void remove(Integer id) {
        if (id == null || id == 0) return;
        store.remove(PREFIX + id);
    }

    @Override
    public List getList() {
        return (List) store.get(PREFIX + POSTFIX);
    }

    @Override
    public void putList(List list) {
        if (list == null || list.size() == 0) return;
        store.put(PREFIX + POSTFIX, list, MAX_AGE);
    }

    @Override
    public void removeList() {
        store.remove(PREFIX + POSTFIX);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
