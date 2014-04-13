package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.ResourceCacheBean;

public interface ResourceStore extends BaseStore<ResourceCacheBean, String> {
    public final String PREFIX = "AppResource-";
    public final String COMMON = "common-";
    public final String COMPANY = "company-";
    public final long MAX_AGE = 0;

    public ResourceCacheBean getCommon(String category);

    public ResourceCacheBean getCompany(String category, Integer companyId);

    public void putCommon(String category, ResourceCacheBean value);

    public void putCompany(String category, Integer companyId, ResourceCacheBean value);
}
