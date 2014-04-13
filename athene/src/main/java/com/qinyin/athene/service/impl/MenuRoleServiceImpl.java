/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppMenuRole;
import com.qinyin.athene.service.MenuRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRoleServiceImpl implements MenuRoleService {
    public static Logger log = LoggerFactory.getLogger(MenuRoleServiceImpl.class);
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
    public AppMenuRole queryById(Integer id) {
        return null;
    }

    @Override
    public List<String> queryRoleListByMenuName(String menuName) {
        List<String> rtnList = new ArrayList<String>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppMenuRole.MENU_NAME, menuName);
        List<Object> list = jdbcDao.queryForList(AppMenuRole.SQL_FILE_NAME, "SELECT", condition, AppMenuRole.class);
        for (Object obj : list) {
            rtnList.add(((AppMenuRole) obj).getRoleName());
        }
        return rtnList;
    }

    @Override
    public int save(AppMenuRole appMenuRole) {
        return 0;
    }

    @Override
    public void update(AppMenuRole appMenuRole) {
    }

    @Override
    public void delete(AppMenuRole appMenuRole) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
