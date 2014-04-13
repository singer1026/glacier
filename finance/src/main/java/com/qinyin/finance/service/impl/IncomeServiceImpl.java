/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.service.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.exception.OptimisticLockException;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.finance.model.Income;
import com.qinyin.finance.service.IncomeService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeServiceImpl implements IncomeService {
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Income.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Income.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(Income.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Income.SQL_FILE_NAME, "QUERY", paramMap, Income.class);
    }

    @Override
    public Income queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Income) jdbcDao.queryForObject(Income.SQL_FILE_NAME, "SELECT", condition, Income.class);
    }

    @Override
    public Income queryFirstByCompanyId(Integer companyId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Income.COMPANY_ID, companyId);
        return (Income) jdbcDao.queryForObject(Income.SQL_FILE_NAME, "SELECT_FIRST", condition, Income.class);
    }

    @Override
    public BigDecimal computeAmountSumByIncomeDate(Integer companyId, Date incomeDate) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Income.COMPANY_ID, companyId);
        condition.put(Income.INCOME_DATE, incomeDate);
        Map<String, Object> map = jdbcDao.queryForMap(Income.SQL_FILE_NAME, "COMPUTE_AMOUNT_SUM_BY_DATE", condition);
        if (map == null) return new BigDecimal("0.0");
        Object amountSum = map.get("amountSum");
        return amountSum != null ? (BigDecimal) amountSum : new BigDecimal("0.0");
    }

    @Override
    public BigDecimal computeAmountSum(Map<String, Object> condition) {
        condition.put(Income.COMPANY_ID, pvgInfo.getCompanyId());
        Map<String, Object> map = jdbcDao.queryForMap(Income.SQL_FILE_NAME, "COMPUTE_AMOUNT_SUM", condition);
        if (map == null) return new BigDecimal("0.0");
        Object amountSum = map.get("amountSum");
        return amountSum != null ? (BigDecimal) amountSum : new BigDecimal("0.0");
    }

    @Override
    public int save(Income income) {
        return jdbcDao.insert(Income.SQL_FILE_NAME, "INSERT", income);
    }

    @Override
    public void update(Income income) {
        if (jdbcDao.update(Income.SQL_FILE_NAME, "UPDATE", income) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Income income) {
        if (jdbcDao.update(Income.SQL_FILE_NAME, "DELETE", income) == 0) {
            throw new OptimisticLockException();
        }
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
