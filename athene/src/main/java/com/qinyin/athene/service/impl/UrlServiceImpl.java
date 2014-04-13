/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppUrl;
import com.qinyin.athene.service.UrlService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlServiceImpl implements UrlService {
    private JdbcDao jdbcDao;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public AppUrl queryById(String name) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppUrl.NAME, name);
        return (AppUrl) jdbcDao.queryForObject(AppUrl.SQL_FILE_NAME, "SELECT", condition, AppUrl.class);
    }

    @Override
    public int save(AppUrl appUrl) {
        return 0;
    }

    @Override
    public void update(AppUrl appUrl) {
    }

    @Override
    public void delete(AppUrl appUrl) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
