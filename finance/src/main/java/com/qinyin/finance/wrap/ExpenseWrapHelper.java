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
import com.qinyin.finance.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseWrapHelper {
    private List expenseWrapList = new ArrayList();
    private ResourceCacheBean expenseCategoryResource;
    private Privilege pvg;

    public ExpenseWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        expenseCategoryResource = resourceProxy.getCompanyBean("expenseCategory");
    }

    public void addWrap(Expense expense) {
        if (expense == null) return;
        expenseWrapList.add(new ExpenseWrap(expense));
    }

    public List getWrappedForList() {
        for (Object obj : expenseWrapList) {
            ExpenseWrap wrap = (ExpenseWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setExpendDateDisplay(DateUtils.formatDate(wrap.getExpendDate()));
            wrap.setCategoryDisplay(expenseCategoryResource.getName(wrap.getCategory()));
            wrap.setAmountDisplay(DigestUtils.formatDecimal(wrap.getAmount()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
        }
        return expenseWrapList;
    }

    public ExpenseWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (ExpenseWrap) wrappedList.get(0) : null;
    }
}
