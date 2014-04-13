/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppCompany;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.service.CompanyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyServiceImpl implements CompanyService {
    private JdbcDao jdbcDao;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public AppCompany queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (AppCompany) jdbcDao.queryForObject(AppCompany.SQL_FILE_NAME, "SELECT", condition, AppCompany.class);
    }

    @Override
    public List<AppCompany> queryAllList() {
        List<AppCompany> rtnList = new ArrayList<AppCompany>();
        List list = jdbcDao.queryForList(AppCompany.SQL_FILE_NAME, "SELECT", null, AppCompany.class);
        for (Object obj : list) {
            rtnList.add((AppCompany) obj);
        }
        return rtnList;
    }

    @Override
    public int save(AppCompany appCompany) {
        return 0;
    }

    @Override
    public void update(AppCompany appCompany) {
    }

    @Override
    public void delete(AppCompany appCompany) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
