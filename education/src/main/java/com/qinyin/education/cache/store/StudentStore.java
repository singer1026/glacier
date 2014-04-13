package com.qinyin.education.cache.store;

import com.qinyin.athene.cache.store.BaseStore;
import com.qinyin.education.cache.model.StudentCacheBean;

public interface StudentStore extends BaseStore<StudentCacheBean, Integer> {
    public final String PREFIX = "Student-";
    public final long MAX_AGE = 0;
}
