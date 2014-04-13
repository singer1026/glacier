/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.wrap;

import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.DigestUtils;
import com.qinyin.finance.model.AccountStatistics;

import java.util.ArrayList;
import java.util.List;

public class AccountStatisticsWrapHelper {
    private List accountStatisticsWrapList = new ArrayList();

    public AccountStatisticsWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
    }

    public void addWrap(AccountStatistics accountStatistics) {
        if (accountStatistics == null) return;
        accountStatisticsWrapList.add(new AccountStatisticsWrap(accountStatistics));
    }

    public List getWrappedForList() {
        for (Object obj : accountStatisticsWrapList) {
            AccountStatisticsWrap wrap = (AccountStatisticsWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDate(wrap.getGmtCreate()));
            wrap.setStatisticsDateDisplay(DateUtils.formatDate(wrap.getStatisticsDate()));
            wrap.setExpenseTotalDisplay(DigestUtils.formatDecimal(wrap.getExpenseTotal()));
            wrap.setIncomeTotalDisplay(DigestUtils.formatDecimal(wrap.getIncomeTotal()));
            wrap.setBalanceDisplay(DigestUtils.formatDecimal(wrap.getBalance()));
            wrap.setDayProfitDisplay(DigestUtils.formatDecimal(wrap.getDayProfit()));
        }
        return accountStatisticsWrapList;
    }

    public AccountStatisticsWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (AccountStatisticsWrap) wrappedList.get(0) : null;
    }
}
