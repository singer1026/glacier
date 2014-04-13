package com.qinyin.finance.model;

import com.qinyin.athene.model.BaseModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AccountStatistics extends BaseModel {
    public static final String GMT_CREATE = "gmtCreate";
    private Timestamp gmtCreate;
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String STATISTICSDATE = "statisticsDate";
    private Timestamp statisticsDate;
    public static final String INCOME_TOTAL = "incomeTotal";
    private BigDecimal incomeTotal;
    public static final String EXPENSE_TOTAL = "expenseTotal";
    private BigDecimal expenseTotal;
    public static final String DAY_PROFIT = "dayProfit";
    private BigDecimal dayProfit;
    public static final String BALANCE = "balance";
    private BigDecimal balance;

    public static final String SQL_FILE_NAME = "FinAccountStatistics";

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Timestamp getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Timestamp statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public BigDecimal getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(BigDecimal expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public BigDecimal getDayProfit() {
        return dayProfit;
    }

    public void setDayProfit(BigDecimal dayProfit) {
        this.dayProfit = dayProfit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}