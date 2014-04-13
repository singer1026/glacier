package com.qinyin.athene.cache.store;

public interface Store {

    public void init();

    public Object get(String key);

    public void put(String key, Object value);

    public void put(String key, Object value, Long maxAge);

    public void remove(String key);

    public void clear();
}
