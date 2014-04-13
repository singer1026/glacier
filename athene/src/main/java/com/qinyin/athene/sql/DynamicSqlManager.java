package com.qinyin.athene.sql;

public interface DynamicSqlManager {
    public void init() throws Exception;

    public String getSql(String key, Object obj);
}
