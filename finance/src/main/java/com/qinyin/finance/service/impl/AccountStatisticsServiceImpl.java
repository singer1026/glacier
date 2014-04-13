/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-21
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.finance.model.AccountStatistics;
import com.qinyin.finance.service.AccountStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountStatisticsServiceImpl implements AccountStatisticsService {
    public static Logger log = LoggerFactory.getLogger(AccountStatisticsServiceImpl.class);
    private PrivilegeInfo pvgInfo;
    private JdbcDao jdbcDao;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(AccountStatistics.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(AccountStatistics.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(AccountStatistics.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(AccountStatistics.SQL_FILE_NAME, "QUERY", paramMap, AccountStatistics.class);
    }

    @Override
    public AccountStatistics queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (AccountStatistics) jdbcDao.queryForObject(AccountStatistics.SQL_FILE_NAME, "SELECT", condition, AccountStatistics.class);
    }

    @Override
    public int save(AccountStatistics accountStatistics) {
        return jdbcDao.insert(AccountStatistics.SQL_FILE_NAME, "INSERT", accountStatistics);
    }

    @Override
    public void update(AccountStatistics accountStatistics) {
        if (jdbcDao.update(AccountStatistics.SQL_FILE_NAME, "UPDATE", accountStatistics) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(AccountStatistics accountStatistics) {
        if (jdbcDao.update(AccountStatistics.SQL_FILE_NAME, "DELETE", accountStatistics) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public AccountStatistics queryByStatisticsDate(Date statisticsDate) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AccountStatistics.COMPANY_ID, pvgInfo.getCompanyId());
        condition.put(AccountStatistics.STATISTICSDATE, statisticsDate);
        return (AccountStatistics) jdbcDao.queryForObject(AccountStatistics.SQL_FILE_NAME, "SELECT", condition, AccountStatistics.class);
    }

    @Override
    public AccountStatistics queryLastByCompanyId(Integer companyId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AccountStatistics.COMPANY_ID, companyId);
        return (AccountStatistics) jdbcDao.queryForObject(AccountStatistics.SQL_FILE_NAME, "SELECT_LAST", condition, AccountStatistics.class);
    }

    @Override
    public void deleteAllByCompanyId(Integer companyId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(AccountStatistics.COMPANY_ID, companyId);
        jdbcDao.update(AccountStatistics.SQL_FILE_NAME, "DELETE_BY_COMPANY_ID", condition);
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
