package com.qinyin.athene.cache.store;

import java.io.Serializable;

public interface BaseStore<T, PK extends Serializable> {

    public T get(PK id);

    public void put(PK id, T value);

    public void remove(PK id);
}
