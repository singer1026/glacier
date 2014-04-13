/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.dao.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.sql.CustomColumnMapRowMapper;
import com.qinyin.athene.sql.DynamicSqlManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

public class JdbcDaoImpl extends SimpleJdbcDaoSupport implements JdbcDao {
    public static Logger log = LoggerFactory.getLogger(JdbcDaoImpl.class);
    private DynamicSqlManager dynamicSqlManager;

    public List queryForList(String fileName, String id, Object obj, Class clazz) {
        String sql = getSql(fileName, id, obj);
        RowMapper mapper = new BeanPropertyRowMapper(clazz);
        SqlParameterSource param = getSqlParameterSource(obj);
        return getSimpleJdbcTemplate().query(sql, mapper, param);
    }

    public List queryForList(String fileName, String id, Object obj) {
        String sql = getSql(fileName, id, obj);
        RowMapper mapper = new CustomColumnMapRowMapper();
        SqlParameterSource param = getSqlParameterSource(obj);
        return getSimpleJdbcTemplate().query(sql, mapper, param);
    }

    public Object queryForObject(String fileName, String id, Object obj, Class clazz) {
        String sql = getSql(fileName, id, obj);
        RowMapper mapper = new BeanPropertyRowMapper(clazz);
        SqlParameterSource param = getSqlParameterSource(obj);
        List list = getSimpleJdbcTemplate().query(sql, mapper, param);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List queryForSingleColumn(String fileName, String id, Object obj, Class clazz) {
        String sql = getSql(fileName, id, obj);
        RowMapper mapper = new SingleColumnRowMapper(clazz);
        SqlParameterSource param = getSqlParameterSource(obj);
        return getSimpleJdbcTemplate().query(sql, mapper, param);
    }

    public Map<String, Object> queryForMap(String fileName, String id, Object obj) {
        String sql = getSql(fileName, id, obj);
        RowMapper mapper = new CustomColumnMapRowMapper();
        SqlParameterSource param = getSqlParameterSource(obj);
        List list = getSimpleJdbcTemplate().query(sql, mapper, param);
        return list.size() > 0 ? (Map<String, Object>) list.get(0) : null;
    }

    public int queryForCount(String fileName, String id, Object obj) {
        String sql = getSql(fileName, id, obj);
        SqlParameterSource param = getSqlParameterSource(obj);
        return getSimpleJdbcTemplate().queryForInt(sql, param);
    }

    public int insert(String fileName, String id, Object obj) {
        String sql = getSql(fileName, id, obj);
        SqlParameterSource param = getSqlParameterSource(obj);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        NamedParameterJdbcOperations operations = getSimpleJdbcTemplate().getNamedParameterJdbcOperations();
        operations.update(sql, param, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int update(String fileName, String id, Object obj) {
        String sql = getSql(fileName, id, obj);
        SqlParameterSource param = getSqlParameterSource(obj);
        return getSimpleJdbcTemplate().update(sql, param);
    }

    public int[] batchUpdate(String fileName, String id, List<Object> list) {
        if (list == null || list.size() == 0) return new int[0];
        int[] rtnArr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rtnArr[i] = update(fileName, id, list.get(i));
        }
        return rtnArr;
    }

    private String getSql(String fileName, String id, Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(fileName).append(".").append(id);
        String sql = dynamicSqlManager.getSql(sb.toString(), obj);
        if (StringUtils.isBlank(sql)) throw new RuntimeException("找不到SQL语句[" + sb.toString() + "]");
        log.debug(sql);
        return sql;
    }

    private SqlParameterSource getSqlParameterSource(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Map) {
            return new MapSqlParameterSource((Map) obj);
        } else {
            return new BeanPropertySqlParameterSource(obj);
        }
    }

    public void setDynamicSqlManager(DynamicSqlManager dynamicSqlManager) {
        this.dynamicSqlManager = dynamicSqlManager;
    }
}
