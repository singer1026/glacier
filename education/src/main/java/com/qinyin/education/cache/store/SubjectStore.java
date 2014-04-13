package com.qinyin.education.cache.store;

import com.qinyin.athene.cache.store.BaseStore;
import com.qinyin.education.cache.model.SubjectCacheBean;

import java.util.List;

public interface SubjectStore extends BaseStore<SubjectCacheBean, Integer> {
    public final String PREFIX = "Subject-";
    public final String POSTFIX = "-List";
    public final long MAX_AGE = 0;

    public List getListByCompanyId(Integer companyId);

    public void putList(Integer companyId, List list);

    public void removeListByCompanyId(Integer companyId);
}
