/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppRole;
import com.qinyin.athene.service.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleServiceImpl implements RoleService {
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
    public AppRole queryById(String name) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppRole.NAME, name);
        return (AppRole) jdbcDao.queryForObject(AppRole.SQL_FILE_NAME, "SELECT", condition, AppRole.class);
    }

    @Override
    public int save(AppRole appRole) {
        return 0;
    }

    @Override
    public void update(AppRole appRole) {
    }

    @Override
    public void delete(AppRole appRole) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
