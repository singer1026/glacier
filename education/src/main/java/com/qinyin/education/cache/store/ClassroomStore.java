package com.qinyin.education.cache.store;

import com.qinyin.athene.cache.store.BaseStore;
import com.qinyin.education.cache.model.ClassroomCacheBean;

import java.util.List;

public interface ClassroomStore extends BaseStore<ClassroomCacheBean, Integer> {
    public final String PREFIX = "Classroom-";
    public final String POSTFIX = "-List";
    public final long MAX_AGE = 0;

    public List getListByCompanyId(Integer companyId);

    public void putList(Integer companyId, List list);

    public void removeByCompanyId(Integer companyId);
}
