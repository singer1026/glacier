package com.qinyin.education.cache.store;

import com.qinyin.athene.cache.store.BaseStore;
import com.qinyin.education.cache.model.LessonCacheBean;

public interface LessonStore extends BaseStore<LessonCacheBean, Integer> {
    public final String PREFIX = "Lesson-";
    public final long MAX_AGE = 0;
}
