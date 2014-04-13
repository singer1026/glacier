/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppOrg;
import com.qinyin.athene.model.AppUserRoleOrg;
import com.qinyin.athene.service.UserRoleOrgService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRoleOrgServiceImpl implements UserRoleOrgService {
    private PrivilegeInfo pvgInfo;
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
    public AppUserRoleOrg queryById(String id) {
        return null;
    }

    @Override
    public List<AppUserRoleOrg> queryListByLoginId(String loginId) {
        List<AppUserRoleOrg> rtnList = new ArrayList<AppUserRoleOrg>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppUserRoleOrg.LOGIN_ID, loginId);
        List<Object> list = jdbcDao.queryForList(AppUserRoleOrg.SQL_FILE_NAME, "SELECT", condition, AppUserRoleOrg.class);
        for (Object obj : list) {
            rtnList.add((AppUserRoleOrg) obj);
        }
        return rtnList;
    }

    @Override
    public List<AppUserRoleOrg> queryListByUserRole(String roleName) {
        List<AppUserRoleOrg> rtnList = new ArrayList<AppUserRoleOrg>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppOrg.COMPANY_ID, pvgInfo.getCompanyId());
        condition.put(AppUserRoleOrg.ROLE_NAME, roleName);
        List<Object> list = jdbcDao.queryForList(AppUserRoleOrg.SQL_FILE_NAME, "SELECT_ACTIVE_BY_ROLE_AND_COMPANY", condition, AppUserRoleOrg.class);
        for (Object obj : list) {
            rtnList.add((AppUserRoleOrg) obj);
        }
        return rtnList;
    }

    @Override
    public int save(AppUserRoleOrg appUserRoleOrg) {
        return 0;
    }

    @Override
    public void update(AppUserRoleOrg appUserRoleOrg) {
    }

    @Override
    public void delete(AppUserRoleOrg appUserRoleOrg) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
