/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppResource;
import com.qinyin.athene.service.ResourceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceServiceImpl implements ResourceService {
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(AppResource.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(AppResource.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(AppResource.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(AppResource.SQL_FILE_NAME, "QUERY", paramMap);
    }

    @Override
    public AppResource queryById(Integer id) {
        return null;
    }

    @Override
    public int save(AppResource appResource) {
        return 0;
    }

    @Override
    public void update(AppResource appResource) {
    }

    @Override
    public void delete(AppResource appResource) {
    }

    @Override
    public List<AppResource> queryCommonList(String category) {
        List<AppResource> rtnList = new ArrayList<AppResource>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppResource.TYPE, ModelConstants.RESOURCE_TYPE_COMMON);
        condition.put(AppResource.CATEGORY, category);
        List<Object> list = jdbcDao.queryForList(AppResource.SQL_FILE_NAME, "SELECT", condition, AppResource.class);
        for (Object obj : list) {
            rtnList.add((AppResource) obj);
        }
        return rtnList;
    }

    @Override
    public List<AppResource> queryCompanyList(String category) {
        List<AppResource> rtnList = new ArrayList<AppResource>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppResource.TYPE, ModelConstants.RESOURCE_TYPE_COMPANY);
        condition.put(AppResource.CATEGORY, category);
        condition.put(AppResource.COMPANY_ID, pvgInfo.getCompanyId());
        List<Object> list = jdbcDao.queryForList(AppResource.SQL_FILE_NAME, "SELECT", condition, AppResource.class);
        for (Object obj : list) {
            rtnList.add((AppResource) obj);
        }
        return rtnList;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
