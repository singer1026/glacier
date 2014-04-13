/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.AppUserRoleOrg;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserCacheBean extends AppUser {
    public static Logger log = LoggerFactory.getLogger(UserCacheBean.class);
    private List<AppUserRoleOrg> userRoleOrgList = new ArrayList<AppUserRoleOrg>();

    public UserCacheBean(AppUser appUser) {
        try {
            PropertyUtils.copyProperties(this, appUser);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public UserCacheBean() {
    }

    public List<AppUserRoleOrg> getUserRoleOrgList() {
        return userRoleOrgList;
    }

    public void setUserRoleOrgList(List<AppUserRoleOrg> userRoleOrgList) {
        this.userRoleOrgList = userRoleOrgList;
    }
}
