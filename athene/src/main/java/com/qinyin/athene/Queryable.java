package com.qinyin.athene;


import java.util.List;
import java.util.Map;

public interface Queryable {
    public int queryForCount(Map<String, Object> paramMap);

    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize);
}
