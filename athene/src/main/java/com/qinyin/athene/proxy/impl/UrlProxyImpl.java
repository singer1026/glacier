package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.UrlCacheBean;
import com.qinyin.athene.cache.store.UrlStore;
import com.qinyin.athene.model.AppUrl;
import com.qinyin.athene.proxy.UrlProxy;
import com.qinyin.athene.service.UrlRoleService;
import com.qinyin.athene.service.UrlService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UrlProxyImpl implements UrlProxy {
    public static Logger log = LoggerFactory.getLogger(UrlProxyImpl.class);
    private UrlStore urlStore;
    private UrlService urlService;
    private UrlRoleService urlRoleService;

    public UrlCacheBean queryByName(String name) {
        if (StringUtils.isBlank(name)) return null;
        UrlCacheBean bean = urlStore.get(name);
        if (bean == null) {
            AppUrl dbAppUrl = urlService.queryById(name);
            if (dbAppUrl == null) {
                log.error("can't find url with name[" + name + "]");
                return null;
            }
            bean = new UrlCacheBean(dbAppUrl);
            List<String> roleList = urlRoleService.queryRoleListByUrlName(bean.getName());
            bean.setRoleList(roleList);
            urlStore.put(bean.getName(), bean);
        }
        return bean;

    }

    public void setUrlStore(UrlStore urlStore) {
        this.urlStore = urlStore;
    }

    public void setUrlService(UrlService urlService) {
        this.urlService = urlService;
    }

    public void setUrlRoleService(UrlRoleService urlRoleService) {
        this.urlRoleService = urlRoleService;
    }
}
