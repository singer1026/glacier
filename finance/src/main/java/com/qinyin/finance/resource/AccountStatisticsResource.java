package com.qinyin.finance.resource;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.QueryableMapping;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.finance.proxy.AccountStatisticsProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

@QueryableMapping("/accountStatisticsQuery")
public class AccountStatisticsResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(AccountStatisticsResource.class);
    private AccountStatisticsProxy accountStatisticsProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return accountStatisticsProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return accountStatisticsProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/accountStatisticsList", type = ReturnType.FREEMARKER, url = "finance/accountStatisticsList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        Date today = DateUtils.getToday();
        paramMap.put("timeStart", DateUtils.previousWeek(today));
        paramMap.put("timeEnd", today);
        return paramMap;
    }

    @UrlMapping(value = "/accountStatisticsTodayView", type = ReturnType.FREEMARKER, url = "finance/accountStatisticsTodayView.ftl")
    public Map<String, Object> viewToday(@Args Map<String, Object> paramMap) {
        paramMap.putAll(accountStatisticsProxy.computeToday());
        return paramMap;
    }

    @UrlMapping(value = "/accountStatisticsTodaySimpleView", type = ReturnType.FREEMARKER, url = "finance/accountStatisticsTodaySimpleView.ftl")
    public Map<String, Object> viewTodaySimple(@Args Map<String, Object> paramMap) {
        paramMap.putAll(accountStatisticsProxy.computeToday());
        return paramMap;
    }

    @UrlMapping(value = "/accountStatisticsGenerate", type = ReturnType.JSON)
    public DataInfoBean generate() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            accountStatisticsProxy.rebuildAll();
        } catch (Exception e) {
            log.error("save expense error", e);
            info.setException(e);
        }
        return info;
    }

    public void setAccountStatisticsProxy(AccountStatisticsProxy accountStatisticsProxy) {
        this.accountStatisticsProxy = accountStatisticsProxy;
    }
}
