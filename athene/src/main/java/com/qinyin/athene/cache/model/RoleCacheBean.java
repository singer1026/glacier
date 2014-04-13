/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppRole;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RoleCacheBean extends AppRole {
    public static Logger log = LoggerFactory.getLogger(RoleCacheBean.class);
    private List<String> userRoleList = new ArrayList<String>();
    private List<String> baseRoleList = new ArrayList<String>();

    public RoleCacheBean(AppRole appRole) {
        try {
            PropertyUtils.copyProperties(this, appRole);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public RoleCacheBean() {
    }

    public List<String> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<String> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public List<String> getBaseRoleList() {
        return baseRoleList;
    }

    public void setBaseRoleList(List<String> baseRoleList) {
        this.baseRoleList = baseRoleList;
    }
}
