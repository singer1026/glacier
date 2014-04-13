/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppUrlRole;
import com.qinyin.athene.service.UrlRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlRoleServiceImpl implements UrlRoleService {
    public static Logger log = LoggerFactory.getLogger(UrlRoleServiceImpl.class);
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
    public AppUrlRole queryById(Integer id) {
        return null;
    }

    @Override
    public List<String> queryRoleListByUrlName(String urlName) {
        List<String> rtnList = new ArrayList<String>();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppUrlRole.URL_NAME, urlName);
        List<Object> list = jdbcDao.queryForList(AppUrlRole.SQL_FILE_NAME, "SELECT", condition, AppUrlRole.class);
        for (Object obj : list) {
            rtnList.add(((AppUrlRole) obj).getRoleName());
        }
        return rtnList;
    }

    @Override
    public int save(AppUrlRole appUrlRole) {
        return 0;
    }

    @Override
    public void update(AppUrlRole appUrlRole) {
    }

    @Override
    public void delete(AppUrlRole appUrlRole) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
