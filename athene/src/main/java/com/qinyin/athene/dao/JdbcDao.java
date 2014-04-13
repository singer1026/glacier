package com.qinyin.athene.dao;

import java.util.List;
import java.util.Map;

public interface JdbcDao {
    public List queryForList(String fileName, String id, Object obj, Class clazz);

    public List queryForList(String fileName, String id, Object obj);

    public Object queryForObject(String fileName, String id, Object obj, Class clazz);

    public List queryForSingleColumn(String fileName, String id, Object obj, Class clazz);

    public Map<String, Object> queryForMap(String fileName, String id, Object obj);

    public int queryForCount(String fileName, String id, Object obj);

    public int insert(String fileName, String id, Object obj);

    public int update(String fileName, String id, Object obj);

    public int[] batchUpdate(String fileName, String id, List<Object> list);
}
