package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.CompanyCacheBean;

import java.util.List;

public interface CompanyStore extends BaseStore<CompanyCacheBean, Integer> {
    public final String PREFIX = "AppCompany-";
    public final String POSTFIX = "List";
    public final long MAX_AGE = 0;

    public List getList();

    public void putList(List list);

    public void removeList();
}
