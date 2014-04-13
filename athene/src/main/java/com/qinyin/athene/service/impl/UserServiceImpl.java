/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-17
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
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
    public int save(AppUser appUser) {
        return jdbcDao.insert(AppUser.SQL_FILE_NAME, "INSERT", appUser);
    }

    @Override
    public void delete(AppUser appUser) {
        if (jdbcDao.update(AppUser.SQL_FILE_NAME, "DELETE", appUser) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public AppUser queryById(String loginId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AppUser.LOGIN_ID, loginId);
        return (AppUser) jdbcDao.queryForObject(AppUser.SQL_FILE_NAME, "SELECT", condition, AppUser.class);
    }

    @Override
    public void updateLogin(AppUser appUser) {
        if (jdbcDao.update(AppUser.SQL_FILE_NAME, "UPDATE_LOGIN", appUser) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void update(AppUser appUser) {
        if (jdbcDao.update(AppUser.SQL_FILE_NAME, "UPDATE", appUser) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void updatePassword(AppUser appUser) {
        if (jdbcDao.update(AppUser.SQL_FILE_NAME, "UPDATE_PASSWORD", appUser) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void updateStatus(AppUser appUser) {
        if (jdbcDao.update(AppUser.SQL_FILE_NAME, "UPDATE_STATUS", appUser) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

}
