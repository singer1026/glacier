/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.cache.store.ResourceStore;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppResource;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.service.ResourceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceProxyImpl implements ResourceProxy {
    public static Logger log = LoggerFactory.getLogger(ResourceProxyImpl.class);
    private ResourceService resourceService;
    private ResourceStore resourceStore;
    private PrivilegeInfo pvgInfo;

    @Override
    public ResourceCacheBean getCommonBean(String category) {
        if (StringUtils.isBlank(category)) return new ResourceCacheBean();
        ResourceCacheBean bean = resourceStore.getCommon(category);
        if (bean == null) {
            List<AppResource> list = resourceService.queryCommonList(category);
            if (list == null || list.size() == 0) {
                log.error("can't find any resources with category[" + category + "]");
                return new ResourceCacheBean();
            }
            bean = new ResourceCacheBean();
            bean.setCategory(category);
            bean.setType(ModelConstants.RESOURCE_TYPE_COMMON);
            bean.setAppResourceList(list);
            resourceStore.putCommon(category, bean);
        }
        return bean;
    }

    @Override
    public ResourceCacheBean getCompanyBean(String category) {
        if (StringUtils.isBlank(category)) return new ResourceCacheBean();
        Integer companyId = pvgInfo.getCompanyId();
        ResourceCacheBean bean = resourceStore.getCompany(category, companyId);
        if (bean == null) {
            List<AppResource> list = resourceService.queryCompanyList(category);
            if (list == null || list.size() == 0) {
                log.error("can't find any resources with category[" + category + "]");
                return new ResourceCacheBean();
            }
            bean = new ResourceCacheBean();
            bean.setCategory(category);
            bean.setType(ModelConstants.RESOURCE_TYPE_COMPANY);
            bean.setCompanyId(companyId);
            bean.setAppResourceList(list);
            resourceStore.putCompany(category, companyId, bean);
        }
        return bean;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void setResourceStore(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
