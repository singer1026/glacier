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
import com.qinyin.finance.proxy.ExpenseProxy;
import com.qinyin.finance.validation.ExpenseValidation;
import com.qinyin.finance.wrap.ExpenseWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

@QueryableMapping("/expenseQuery")
public class ExpenseResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(ExpenseResource.class);
    private ExpenseProxy expenseProxy;
    private ExpenseValidation expenseValidation;
    private ResourceProxy resourceProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return expenseProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return expenseProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/expenseList", type = ReturnType.FREEMARKER, url = "finance/expenseList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("expenseCategoryList", resourceProxy.getCompanyBean("expenseCategory").getAppResourceList());
        Date today = DateUtils.getToday();
        paramMap.put("timeStart", today);
        paramMap.put("timeEnd", today);
        return paramMap;
    }

    @UrlMapping(value = "/expenseAdd", type = ReturnType.FREEMARKER, url = "finance/expenseEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) {
        paramMap.put("expenseCategoryList", resourceProxy.getCompanyBean("expenseCategory").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/expenseEdit", type = ReturnType.FREEMARKER, url = "finance/expenseEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("expenseCategoryList", resourceProxy.getCompanyBean("expenseCategory").getAppResourceList());
        return queryById(paramMap);
    }

    @UrlMapping(value = "/expenseView", type = ReturnType.FREEMARKER, url = "finance/expenseView.ftl")
    public Map<String, Object> view(@Args Map<String, Object> paramMap) throws Exception {
        return queryById(paramMap);
    }

    private Map<String, Object> queryById(Map<String, Object> paramMap) throws Exception {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            ExpenseWrap wrap = expenseProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("expense", wrap);
            }
        }
        return paramMap;
    }

    @UrlMapping(value = "/expenseSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = expenseValidation.simpleValidate();
            if (info.hasError()) return info;
            expenseProxy.save();
        } catch (Exception e) {
            log.error("save expense error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/expenseUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = expenseValidation.simpleValidate();
            if (info.hasError()) return info;
            expenseProxy.update();
        } catch (Exception e) {
            log.error("update expense error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/expenseDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            expenseProxy.delete();
        } catch (Exception e) {
            log.error("delete expense error", e);
            info.setException(e);
        }
        return info;
    }

    public void setExpenseProxy(ExpenseProxy expenseProxy) {
        this.expenseProxy = expenseProxy;
    }

    public void setExpenseValidation(ExpenseValidation expenseValidation) {
        this.expenseValidation = expenseValidation;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }
}
