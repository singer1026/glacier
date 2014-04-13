/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppUrl;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UrlCacheBean extends AppUrl {
    public static Logger log = LoggerFactory.getLogger(UrlCacheBean.class);
    private List<String> roleList = new ArrayList<String>();

    public UrlCacheBean(AppUrl appUrl) {
        try {
            PropertyUtils.copyProperties(this, appUrl);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public UrlCacheBean() {
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }
}
