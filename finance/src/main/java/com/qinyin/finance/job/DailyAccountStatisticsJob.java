/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-22
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.job;

import com.qinyin.athene.cache.model.CompanyCacheBean;
import com.qinyin.athene.proxy.CompanyProxy;
import com.qinyin.finance.proxy.AccountStatisticsProxy;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class DailyAccountStatisticsJob extends QuartzJobBean {
    public static Logger log = LoggerFactory.getLogger(DailyAccountStatisticsJob.class);
    private AccountStatisticsProxy accountStatisticsProxy;
    private CompanyProxy companyProxy;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<CompanyCacheBean> companyCacheBeanList = companyProxy.queryAllList();
        for (CompanyCacheBean companyCacheBean : companyCacheBeanList) {
            try {
                accountStatisticsProxy.buildLastDays(companyCacheBean.getId());
            } catch (Exception e) {
                log.error("catch exception ", e);
            }
        }
    }

    public void setAccountStatisticsProxy(AccountStatisticsProxy accountStatisticsProxy) {
        this.accountStatisticsProxy = accountStatisticsProxy;
    }

    public void setCompanyProxy(CompanyProxy companyProxy) {
        this.companyProxy = companyProxy;
    }
}
