/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.RoleCacheBean;
import com.qinyin.athene.cache.store.RoleStore;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.model.AppRole;
import com.qinyin.athene.proxy.RoleProxy;
import com.qinyin.athene.service.RoleBaseRoleService;
import com.qinyin.athene.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoleProxyImpl implements RoleProxy {
    public static Logger log = LoggerFactory.getLogger(RoleProxyImpl.class);
    private RoleService roleService;
    private RoleStore roleStore;
    private RoleBaseRoleService roleBaseRoleService;

    @Override
    public RoleCacheBean queryByName(String roleName) {
        if (StringUtils.isBlank(roleName)) return null;
        RoleCacheBean bean = roleStore.get(roleName);
        if (bean == null) {
            AppRole dbAppRole = roleService.queryById(roleName);
            if (dbAppRole == null) {
                log.error("can't find role with name[" + roleName + "]");
                return null;
            }
            bean = new RoleCacheBean(dbAppRole);
            String roleType = bean.getType();
            if (roleType.equals(ModelConstants.ROLE_TYPE_BASE)) {
                List<String> userRoleList = roleBaseRoleService.queryUserRoleByBaseRole(roleName);
                bean.setUserRoleList(userRoleList);
            } else {
                List<String> baseRoleList = roleBaseRoleService.queryBaseRoleByUserRole(roleName);
                bean.setBaseRoleList(baseRoleList);
            }
            roleStore.put(roleName, bean);
        }
        return bean;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setRoleStore(RoleStore roleStore) {
        this.roleStore = roleStore;
    }

    public void setRoleBaseRoleService(RoleBaseRoleService roleBaseRoleService) {
        this.roleBaseRoleService = roleBaseRoleService;
    }
}
