/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.AppWallpaperLink;
import com.qinyin.athene.service.WallpaperLinkService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallpaperLinkServiceImpl implements WallpaperLinkService {
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
    public AppWallpaperLink queryById(String filePath) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppWallpaperLink.FILE_PATH, filePath);
        return (AppWallpaperLink) jdbcDao.queryForObject(AppWallpaperLink.SQL_FILE_NAME, "SELECT", condition, AppWallpaperLink.class);
    }

    @Override
    public int save(AppWallpaperLink appWallpaperLink) {
        return 0;
    }

    @Override
    public void update(AppWallpaperLink appWallpaperLink) {
    }

    @Override
    public void delete(AppWallpaperLink appWallpaperLink) {
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
