/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppRoleBaseRole;
import com.qinyin.athene.service.RoleBaseRoleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleBaseRoleServiceImpl implements RoleBaseRoleService {
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
    public AppRoleBaseRole queryById(String id) {
        return null;
    }

    @Override
    public List<String> queryBaseRoleByUserRole(String roleName) {
        List<String> rtnList = new ArrayList<String>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppRoleBaseRole.ROLE_NAME, roleName);
        List<Object> list = jdbcDao.queryForList(AppRoleBaseRole.SQL_FILE_NAME, "SELECT", condition, AppRoleBaseRole.class);
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                rtnList.add(((AppRoleBaseRole) obj).getBaseRoleName());
            }
        }
        return rtnList;
    }

    @Override
    public List<String> queryUserRoleByBaseRole(String baseRoleName) {
        List<String> rtnList = new ArrayList<String>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppRoleBaseRole.BASE_ROLE_NAME, baseRoleName);
        List<Object> list = jdbcDao.queryForList(AppRoleBaseRole.SQL_FILE_NAME, "SELECT", condition, AppRoleBaseRole.class);
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                rtnList.add(((AppRoleBaseRole) obj).getRoleName());
            }
        }
        return rtnList;
    }

    @Override
    public int save(AppRoleBaseRole appRoleBaseRole) {
        return 0;
    }

    @Override
    public void update(AppRoleBaseRole appRoleBaseRole) {
    }

    @Override
    public void delete(AppRoleBaseRole appRoleBaseRole) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
