/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-21
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.proxy.impl;

import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppResource;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.DigestUtils;
import com.qinyin.finance.model.AccountStatistics;
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.model.Income;
import com.qinyin.finance.proxy.AccountStatisticsProxy;
import com.qinyin.finance.service.AccountStatisticsService;
import com.qinyin.finance.service.ExpenseService;
import com.qinyin.finance.service.IncomeService;
import com.qinyin.finance.wrap.AccountStatisticsWrapHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

public class AccountStatisticsProxyImpl implements AccountStatisticsProxy {
    public static Logger log = LoggerFactory.getLogger(AccountStatisticsProxyImpl.class);
    private ResourceProxy resourceProxy;
    private AccountStatisticsService accountStatisticsService;
    private ExpenseService expenseService;
    private IncomeService incomeService;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return accountStatisticsService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        AccountStatisticsWrapHelper helper = new AccountStatisticsWrapHelper();
        List accountStatisticsList = accountStatisticsService.queryForList(paramMap);
        for (Object obj : accountStatisticsList) {
            helper.addWrap((AccountStatistics) obj);
        }
        return helper.getWrappedForList();
    }

    @Override
    public Map<String, Object> computeToday() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Integer companyId = pvgInfo.getCompanyId();
        Date today = DateUtils.getToday();
        BigDecimal expenseTotal = expenseService.computeAmountSumByExpendDate(companyId, today);
        BigDecimal incomeTotal = incomeService.computeAmountSumByIncomeDate(companyId, today);
        BigDecimal dayProfit = incomeTotal.subtract(expenseTotal);
        Date yesterday = DateUtils.previousDay(today);
        AccountStatistics dbAs = accountStatisticsService.queryByStatisticsDate(yesterday);
        BigDecimal yesterdayBalance = null;
        BigDecimal yesterdayProfit = null;
        BigDecimal todayBalance = null;
        if (dbAs != null) {
            yesterdayBalance = dbAs.getBalance();
            yesterdayProfit = dbAs.getDayProfit();
            todayBalance = yesterdayBalance.add(dayProfit);
        }
        rtnMap.put("expenseTotal", DigestUtils.formatDecimal(expenseTotal));
        rtnMap.put("incomeTotal", DigestUtils.formatDecimal(incomeTotal));
        rtnMap.put("dayProfit", DigestUtils.formatDecimal(dayProfit));
        if (yesterdayBalance != null) {
            rtnMap.put("yesterdayBalance", DigestUtils.formatDecimal(yesterdayBalance));
        } else {
            rtnMap.put("yesterdayBalance", "无数据");
        }
        if (yesterdayProfit != null) {
            rtnMap.put("yesterdayProfit", DigestUtils.formatDecimal(yesterdayProfit));
        } else {
            rtnMap.put("yesterdayProfit", "无数据");
        }
        if (todayBalance != null) {
            rtnMap.put("todayBalance", DigestUtils.formatDecimal(todayBalance));
        } else {
            rtnMap.put("todayBalance", "无法计算");
        }
        return rtnMap;
    }

    @Override
    public void rebuildAll() {
        rebuildAll(pvgInfo.getCompanyId());
    }

    @Override
    public void rebuildAll(Integer companyId) {
        if (companyId == null || companyId == 0) throw new RuntimeException("companyId is null or equal 0");
        Expense earlyExpense = expenseService.queryFirstByCompanyId(companyId);
        Income earlyIncome = incomeService.queryFirstByCompanyId(companyId);
        Date computeDate = null;
        if (earlyExpense != null && earlyIncome == null) {
            computeDate = earlyExpense.getExpendDate();
        } else if (earlyExpense == null && earlyIncome != null) {
            computeDate = earlyIncome.getIncomeDate();
        } else if (earlyExpense != null && earlyIncome != null) {
            if (earlyExpense.getExpendDate().getTime() < earlyIncome.getIncomeDate().getTime()) {
                computeDate = earlyExpense.getExpendDate();
            } else {
                computeDate = earlyIncome.getIncomeDate();
            }
        }
        if (computeDate == null) return;
        /**start**/
        accountStatisticsService.deleteAllByCompanyId(companyId);
        Date today = DateUtils.getToday();
        BigDecimal balance = new BigDecimal(0);
        while (DateUtils.compare(computeDate, today) < 0) {
            BigDecimal expenseTotal = expenseService.computeAmountSumByExpendDate(companyId, computeDate);
            BigDecimal incomeTotal = incomeService.computeAmountSumByIncomeDate(companyId, computeDate);
            BigDecimal dayProfit = incomeTotal.subtract(expenseTotal);
            balance = balance.add(dayProfit);
            AccountStatistics as = new AccountStatistics();
            as.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            as.setCompanyId(companyId);
            as.setStatisticsDate(DateUtils.cloneTimestamp(computeDate));
            as.setExpenseTotal(expenseTotal);
            as.setIncomeTotal(incomeTotal);
            as.setDayProfit(dayProfit);
            as.setBalance(balance);
            accountStatisticsService.save(as);
            computeDate = DateUtils.nextDay(computeDate);
        }
    }

    @Override
    public void buildLastDays(Integer companyId) {
        if (companyId == null || companyId == 0) throw new RuntimeException("companyId is null or equal 0");
        AccountStatistics dbAs = accountStatisticsService.queryLastByCompanyId(companyId);
        if (dbAs == null) {
            this.rebuildAll(companyId);
            return;
        }
        Date today = DateUtils.getToday();
        BigDecimal balanceBase = dbAs.getBalance();
        Date computeDate = DateUtils.nextDay(dbAs.getStatisticsDate());
        while (DateUtils.compare(computeDate, today) < 0) {
            BigDecimal expenseTotal = expenseService.computeAmountSumByExpendDate(companyId, computeDate);
            BigDecimal incomeTotal = incomeService.computeAmountSumByIncomeDate(companyId, computeDate);
            BigDecimal dayProfit = incomeTotal.subtract(expenseTotal);
            balanceBase = balanceBase.add(dayProfit);
            AccountStatistics as = new AccountStatistics();
            as.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            as.setCompanyId(companyId);
            as.setStatisticsDate(DateUtils.cloneTimestamp(computeDate));
            as.setExpenseTotal(expenseTotal);
            as.setIncomeTotal(incomeTotal);
            as.setDayProfit(dayProfit);
            as.setBalance(balanceBase);
            accountStatisticsService.save(as);
            computeDate = DateUtils.nextDay(computeDate);
        }
    }

    @Override
    public List computeExpenseCategoryAmountSum(Date start, Date end) {
        List rtnList = new ArrayList();
        List<AppResource> expenseCategoryList = resourceProxy.getCompanyBean("expenseCategory").getAppResourceList();
        for (AppResource appResource : expenseCategoryList) {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("expendDateStart", DateUtils.formatDate(start));
            condition.put("expendDateEnd", DateUtils.formatDate(end));
            condition.put(Expense.CATEGORY, appResource.getValue());
            condition.put(Expense.COMPANY_ID, pvgInfo.getCompanyId());
            BigDecimal amountSum = expenseService.computeAmountSum(condition);
            result.put("categoryName", appResource.getName());
            result.put("result", DigestUtils.formatDecimal(amountSum));
            rtnList.add(result);
        }
        return rtnList;
    }

    @Override
    public List computeIncomeCategoryAmountSum(Date start, Date end) {
        List rtnList = new ArrayList();
        List<AppResource> incomeCategoryList = resourceProxy.getCompanyBean("incomeCategory").getAppResourceList();
        for (AppResource appResource : incomeCategoryList) {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("incomeDateStart", DateUtils.formatDate(start));
            condition.put("incomeDateEnd", DateUtils.formatDate(end));
            condition.put(Income.CATEGORY, appResource.getValue());
            condition.put(Income.COMPANY_ID, pvgInfo.getCompanyId());
            BigDecimal amountSum = incomeService.computeAmountSum(condition);
            result.put("categoryName", appResource.getName());
            result.put("result", DigestUtils.formatDecimal(amountSum));
            rtnList.add(result);
        }
        return rtnList;
    }

    public void setAccountStatisticsService(AccountStatisticsService accountStatisticsService) {
        this.accountStatisticsService = accountStatisticsService;
    }

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void setIncomeService(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }
}
