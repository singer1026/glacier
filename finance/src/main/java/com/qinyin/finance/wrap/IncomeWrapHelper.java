/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.wrap;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.DigestUtils;
import com.qinyin.finance.model.Income;

import java.util.ArrayList;
import java.util.List;

public class IncomeWrapHelper {
    private List incomeWrapList = new ArrayList();
    private ResourceCacheBean incomeCategoryResource;
    private Privilege pvg;


    public IncomeWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        incomeCategoryResource = resourceProxy.getCompanyBean("incomeCategory");
    }

    public void addWrap(Income income) {
        if (income == null) return;
        incomeWrapList.add(new IncomeWrap(income));
    }

    public List getWrappedForList() {
        for (Object obj : incomeWrapList) {
            IncomeWrap wrap = (IncomeWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setIncomeDateDisplay(DateUtils.formatDate(wrap.getIncomeDate()));
            wrap.setCategoryDisplay(incomeCategoryResource.getName(wrap.getCategory()));
            wrap.setAmountDisplay(DigestUtils.formatDecimal(wrap.getAmount()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
        }
        return incomeWrapList;
    }

    public IncomeWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (IncomeWrap) wrappedList.get(0) : null;
    }
}
