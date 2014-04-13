/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppMenu;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuCacheBean extends AppMenu {
    public static Logger log = LoggerFactory.getLogger(MenuCacheBean.class);
    private List<String> roleList = new ArrayList<String>();

    public MenuCacheBean(AppMenu appMenu) {
        try {
            PropertyUtils.copyProperties(this, appMenu);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public MenuCacheBean() {
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
