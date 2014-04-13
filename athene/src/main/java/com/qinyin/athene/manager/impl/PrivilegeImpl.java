/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-17
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.manager.impl;

import com.qinyin.athene.cache.model.RoleCacheBean;
import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.model.AppUserRoleOrg;
import com.qinyin.athene.proxy.RoleProxy;
import com.qinyin.athene.proxy.UserProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrivilegeImpl implements Privilege {
    public static Logger log = LoggerFactory.getLogger(PrivilegeImpl.class);
    private JdbcDao jdbcDao;
    private UserProxy userProxy;
    private RoleProxy roleProxy;

    @Override
    public List<String> getUserRoleListByLoginId(String loginId) {
        List<String> userRoleList = new ArrayList<String>();
        UserCacheBean userBean = userProxy.queryByLoginId(loginId);
        if (userBean == null) return userRoleList;
        List<AppUserRoleOrg> userRoleOrgList = userBean.getUserRoleOrgList();
        for (AppUserRoleOrg appUserRoleOrg : userRoleOrgList) {
            userRoleList.add(appUserRoleOrg.getRoleName());
        }
        return userRoleList;
    }

    @Override
    public List<String> getBaseRoleListByLoginId(String loginId) {
        UserCacheBean userBean = userProxy.queryByLoginId(loginId);
        if (userBean == null) return new ArrayList<String>(0);
        Set<String> baseRoleSet = new HashSet<String>();
        List<AppUserRoleOrg> userRoleOrgList = userBean.getUserRoleOrgList();
        for (AppUserRoleOrg appUserRoleOrg : userRoleOrgList) {
            String userRoleName = appUserRoleOrg.getRoleName();
            RoleCacheBean roleBean = roleProxy.queryByName(userRoleName);
            if (roleBean.getType().equals(ModelConstants.ROLE_TYPE_BASE)) {
                baseRoleSet.add(roleBean.getName());
                continue;
            }
            List<String> baseRoleList = roleBean.getBaseRoleList();
            baseRoleSet.addAll(baseRoleList);
        }
        return new ArrayList<String>(baseRoleSet);
    }

    public String getUserNameByLoginId(String loginId) {
        UserCacheBean userCacheBean = userProxy.queryByLoginId(loginId);
        return userCacheBean != null ? userCacheBean.getName() : null;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setRoleProxy(RoleProxy roleProxy) {
        this.roleProxy = roleProxy;
    }
}
