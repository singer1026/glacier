/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppMenu;
import com.qinyin.athene.service.MenuService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuServiceImpl implements MenuService {
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
    public AppMenu queryById(String name) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppMenu.NAME, name);
        return (AppMenu) jdbcDao.queryForObject(AppMenu.SQL_FILE_NAME, "SELECT", condition, AppMenu.class);
    }

    @Override
    public List<AppMenu> queryAllListByType(String type) {
        List<AppMenu> rtnList = new ArrayList<AppMenu>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppMenu.TYPE, type);
        List list = jdbcDao.queryForList(AppMenu.SQL_FILE_NAME, "SELECT", condition, AppMenu.class);
        for (Object obj : list) {
            rtnList.add((AppMenu) obj);
        }
        return rtnList;
    }

    @Override
    public int save(AppMenu appMenu) {
        return 0;
    }

    @Override
    public void update(AppMenu appMenu) {
    }

    @Override
    public void delete(AppMenu appMenu) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
