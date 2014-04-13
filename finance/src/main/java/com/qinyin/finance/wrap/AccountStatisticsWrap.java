/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.wrap;

import com.qinyin.finance.model.AccountStatistics;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class AccountStatisticsWrap {
    public static Logger log = LoggerFactory.getLogger(AccountStatisticsWrap.class);
    private Integer id;
    private Timestamp gmtCreate;
    private String gmtCreateDisplay;
    private Timestamp statisticsDate;
    private String statisticsDateDisplay;
    private BigDecimal incomeTotal;
    private String incomeTotalDisplay;
    private BigDecimal expenseTotal;
    private String expenseTotalDisplay;
    private BigDecimal dayProfit;
    private String dayProfitDisplay;
    private BigDecimal balance;
    private String balanceDisplay;

    public AccountStatisticsWrap() {
    }

    public AccountStatisticsWrap(AccountStatistics accountStatistics) {
        try {
            PropertyUtils.copyProperties(this, accountStatistics);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtCreateDisplay() {
        return gmtCreateDisplay;
    }

    public void setGmtCreateDisplay(String gmtCreateDisplay) {
        this.gmtCreateDisplay = gmtCreateDisplay;
    }

    public Timestamp getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Timestamp statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public String getStatisticsDateDisplay() {
        return statisticsDateDisplay;
    }

    public void setStatisticsDateDisplay(String statisticsDateDisplay) {
        this.statisticsDateDisplay = statisticsDateDisplay;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public String getIncomeTotalDisplay() {
        return incomeTotalDisplay;
    }

    public void setIncomeTotalDisplay(String incomeTotalDisplay) {
        this.incomeTotalDisplay = incomeTotalDisplay;
    }

    public BigDecimal getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(BigDecimal expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    public String getExpenseTotalDisplay() {
        return expenseTotalDisplay;
    }

    public void setExpenseTotalDisplay(String expenseTotalDisplay) {
        this.expenseTotalDisplay = expenseTotalDisplay;
    }

    public BigDecimal getDayProfit() {
        return dayProfit;
    }

    public void setDayProfit(BigDecimal dayProfit) {
        this.dayProfit = dayProfit;
    }

    public String getDayProfitDisplay() {
        return dayProfitDisplay;
    }

    public void setDayProfitDisplay(String dayProfitDisplay) {
        this.dayProfitDisplay = dayProfitDisplay;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBalanceDisplay() {
        return balanceDisplay;
    }

    public void setBalanceDisplay(String balanceDisplay) {
        this.balanceDisplay = balanceDisplay;
    }
}
