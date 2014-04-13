/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.impl;

import com.qinyin.athene.cache.store.Store;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalStore implements Store {
    public static Logger log = LoggerFactory.getLogger(LocalStore.class);
    private Map<String, LocalCacheBean> store = new ConcurrentHashMap<String, LocalCacheBean>();
    private long maxAge = 1000 * 60 * 60 * 4;

    @Override
    public void init() {
    }

    @Override
    public Object get(String key) {
        if (StringUtils.isBlank(key)) return null;
        LocalCacheBean localCacheBean = store.get(key);
        if (localCacheBean == null) return null;
        if (localCacheBean.isAvailable()) {
            log.debug("get one bean from cache , key[" + key + "]");
            return localCacheBean.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void put(String key, Object value) {
        this.put(key, value, this.maxAge);
    }

    @Override
    public void put(String key, Object value, Long maxAge) {
        LocalCacheBean localCacheBean = new LocalCacheBean();
        localCacheBean.setMaxAge(maxAge);
        localCacheBean.setKey(key);
        localCacheBean.setValue(value);
        store.put(key, localCacheBean);
        log.debug("put one bean to cache , key[" + key + "]");
    }

    @Override
    public void remove(String key) {
        store.remove(key);
        log.debug("remove one bean from cache , key[" + key + "]");
    }

    @Override
    public void clear() {
        store.clear();
    }
}
