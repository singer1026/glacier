package com.qinyin.education.cache.store;

import com.qinyin.athene.cache.store.BaseStore;
import com.qinyin.education.cache.model.TeacherCacheBean;

public interface TeacherStore extends BaseStore<TeacherCacheBean, Integer> {
    public final String PREFIX = "Teacher-";
    public final long MAX_AGE = 0;
}
