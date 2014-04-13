/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.store.impl;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.cache.store.ResourceStore;
import com.qinyin.athene.cache.store.Store;
import org.apache.commons.lang.StringUtils;

public class ResourceStoreImpl implements ResourceStore {
    private Store store;

    @Override
    public ResourceCacheBean getCommon(String category) {
        return this.get(COMMON + category);
    }

    @Override
    public ResourceCacheBean getCompany(String category, Integer companyId) {
        return this.get(COMPANY + companyId + "-" + category);
    }

    @Override
    public void putCommon(String category, ResourceCacheBean value) {
        this.put(COMMON + category, value);
    }

    @Override
    public void putCompany(String category, Integer companyId, ResourceCacheBean value) {
        this.put(COMPANY + companyId + "-" + category, value);
    }

    @Override
    public ResourceCacheBean get(String id) {
        if (StringUtils.isBlank(id)) return null;
        Object obj = store.get(PREFIX + id);
        return obj == null ? null : (ResourceCacheBean) obj;
    }

    @Override
    public void put(String id, ResourceCacheBean value) {
        if (StringUtils.isBlank(id) || value == null) return;
        store.put(PREFIX + id, value, MAX_AGE);
    }

    @Override
    public void remove(String id) {
        if (StringUtils.isBlank(id)) return;
        store.remove(PREFIX + id);
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
