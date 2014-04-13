/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppResource;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ResourceCacheBean {
    private List<AppResource> appResourceList = new ArrayList<AppResource>();
    private String type;
    private Integer companyId;
    private String category;

    public List<AppResource> getAppResourceList() {
        return appResourceList;
    }

    public void setAppResourceList(List<AppResource> appResourceList) {
        this.appResourceList = appResourceList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName(String value) {
        for (AppResource appResource : appResourceList) {
            if (appResource.getValue().equals(value)) {
                return appResource.getName();
            }
        }
        return StringUtils.EMPTY;
    }
}
