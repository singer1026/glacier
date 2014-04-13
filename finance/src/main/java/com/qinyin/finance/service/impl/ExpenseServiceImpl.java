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
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.service.ExpenseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseServiceImpl implements ExpenseService {
    private JdbcDao jdbcDao;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        paramMap.put(Expense.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForCount(Expense.SQL_FILE_NAME, "QUERY_COUNT", paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        paramMap.put(Expense.COMPANY_ID, pvgInfo.getCompanyId());
        return jdbcDao.queryForList(Expense.SQL_FILE_NAME, "QUERY", paramMap, Expense.class);
    }

    @Override
    public Expense queryById(Integer id) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(BaseModel.ID, id);
        return (Expense) jdbcDao.queryForObject(Expense.SQL_FILE_NAME, "SELECT", condition, Expense.class);
    }

    @Override
    public Expense queryFirstByCompanyId(Integer companyId) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Expense.COMPANY_ID, companyId);
        return (Expense) jdbcDao.queryForObject(Expense.SQL_FILE_NAME, "SELECT_FIRST", condition, Expense.class);
    }

    @Override
    public BigDecimal computeAmountSumByExpendDate(Integer companyId, Date expendDate) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put(Expense.COMPANY_ID, companyId);
        condition.put(Expense.EXPEND_DATE, expendDate);
        Map<String, Object> map = jdbcDao.queryForMap(Expense.SQL_FILE_NAME, "COMPUTE_AMOUNT_SUM_BY_DATE", condition);
        if (map == null) return new BigDecimal("0.0");
        Object amountSum = map.get("amountSum");
        return amountSum != null ? (BigDecimal) amountSum : new BigDecimal("0.0");
    }

    @Override
    public BigDecimal computeAmountSum(Map<String, Object> condition) {
        condition.put(Expense.COMPANY_ID, pvgInfo.getCompanyId());
        Map<String, Object> map = jdbcDao.queryForMap(Expense.SQL_FILE_NAME, "COMPUTE_AMOUNT_SUM", condition);
        if (map == null) return new BigDecimal("0.0");
        Object amountSum = map.get("amountSum");
        return amountSum != null ? (BigDecimal) amountSum : new BigDecimal("0.0");
    }

    @Override
    public int save(Expense expense) {
        return jdbcDao.insert(Expense.SQL_FILE_NAME, "INSERT", expense);
    }

    @Override
    public void update(Expense expense) {
        if (jdbcDao.update(Expense.SQL_FILE_NAME, "UPDATE", expense) == 0) {
            throw new OptimisticLockException();
        }
    }

    @Override
    public void delete(Expense expense) {
        if (jdbcDao.update(Expense.SQL_FILE_NAME, "DELETE", expense) == 0) {
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
