/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-7
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.resource;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.QueryableMapping;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.finance.proxy.IncomeProxy;
import com.qinyin.finance.validation.IncomeValidation;
import com.qinyin.finance.wrap.IncomeWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

@QueryableMapping("/incomeQuery")
public class IncomeResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(IncomeResource.class);
    private ResourceProxy resourceProxy;
    private IncomeProxy incomeProxy;
    private IncomeValidation incomeValidation;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return incomeProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return incomeProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/incomeList", type = ReturnType.FREEMARKER, url = "finance/incomeList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("incomeCategoryList", resourceProxy.getCompanyBean("incomeCategory").getAppResourceList());
        Date today = DateUtils.getToday();
        paramMap.put("timeStart", today);
        paramMap.put("timeEnd", today);
        return paramMap;
    }

    @UrlMapping(value = "/incomeAdd", type = ReturnType.FREEMARKER, url = "finance/incomeEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) {
        paramMap.put("incomeCategoryList", resourceProxy.getCompanyBean("incomeCategory").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/incomeEdit", type = ReturnType.FREEMARKER, url = "finance/incomeEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("incomeCategoryList", resourceProxy.getCompanyBean("incomeCategory").getAppResourceList());
        return queryById(paramMap);
    }

    @UrlMapping(value = "/incomeView", type = ReturnType.FREEMARKER, url = "finance/incomeView.ftl")
    public Map<String, Object> view(@Args Map<String, Object> paramMap) throws Exception {
        return queryById(paramMap);
    }

    private Map<String, Object> queryById(Map<String, Object> paramMap) throws Exception {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            IncomeWrap wrap = incomeProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("income", wrap);
            }
        }
        return paramMap;
    }

    @UrlMapping(value = "/incomeSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = incomeValidation.simpleValidate();
            if (info.hasError()) return info;
            incomeProxy.save();
        } catch (Exception e) {
            log.error("save income error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/incomeUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = incomeValidation.simpleValidate();
            if (info.hasError()) return info;
            incomeProxy.update();
        } catch (Exception e) {
            log.error("update income error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/incomeDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            incomeProxy.delete();
        } catch (Exception e) {
            log.error("delete income error", e);
            info.setException(e);
        }
        return info;
    }

    public void setIncomeProxy(IncomeProxy incomeProxy) {
        this.incomeProxy = incomeProxy;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setIncomeValidation(IncomeValidation incomeValidation) {
        this.incomeValidation = incomeValidation;
    }
}
