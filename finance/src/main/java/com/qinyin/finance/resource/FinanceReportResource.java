/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-7
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FinanceReportResource {
    public static Logger log = LoggerFactory.getLogger(FinanceReportResource.class);

    @UrlMapping(value = "/financeReportList", type = ReturnType.FREEMARKER, url = "finance/financeReportList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("timeStart", DateUtils.getNow());
        paramMap.put("timeEnd", DateUtils.getNow());
        return paramMap;
    }
}
