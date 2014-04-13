/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.proxy.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.athene.util.PermissionUtils;
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.proxy.ExpenseProxy;
import com.qinyin.finance.service.ExpenseService;
import com.qinyin.finance.wrap.ExpenseWrap;
import com.qinyin.finance.wrap.ExpenseWrapHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ExpenseProxyImpl implements ExpenseProxy {
    public static Logger log = LoggerFactory.getLogger(ExpenseProxyImpl.class);
    private ExpenseService expenseService;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return expenseService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        ExpenseWrapHelper helper = new ExpenseWrapHelper();
        List expenseList = expenseService.queryForList(paramMap);
        for (Object obj : expenseList) {
            helper.addWrap((Expense) obj);
        }
        return helper.getWrappedForList();
    }

    private Expense getNewExpense() {
        String creator = pvgInfo.getLoginId();
        Integer companyId = pvgInfo.getCompanyId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Expense expense = new Expense();
        expense.setGmtCreate(now);
        expense.setCreator(creator);
        expense.setGmtModify(now);
        expense.setModifier(creator);
        expense.setIsDeleted(ModelConstants.IS_DELETED_N);
        expense.setVersion(ModelConstants.VERSION_INIT);
        expense.setCompanyId(companyId);
        expense.setCategory(ParamUtils.getString(paramMap.get(Expense.CATEGORY)));
        expense.setTitle(ParamUtils.getString(paramMap.get(Expense.TITLE)));
        expense.setDescription(ParamUtils.getString(paramMap.get(Expense.DESCRIPTION)));
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Expense.FULL_EDIT_ROLE)) {
            expense.setExpendDate(ParamUtils.getTimestamp(paramMap.get(Expense.EXPEND_DATE)));
        } else {
            expense.setExpendDate(now);
        }
        expense.setAmount(ParamUtils.getBigDecimal(paramMap.get(Expense.AMOUNT)));
        return expense;
    }

    private Expense getUpdateExpense(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Expense dbExpense = this.queryById(id);
        Expense orphanExpense = null;
        try {
            orphanExpense = (Expense) BeanUtils.cloneBean(dbExpense);
            orphanExpense.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanExpense.setModifier(pvgInfo.getLoginId());
            orphanExpense.setCategory(ParamUtils.getString(paramMap.get(Expense.CATEGORY)));
            orphanExpense.setTitle(ParamUtils.getString(paramMap.get(Expense.TITLE)));
            orphanExpense.setDescription(ParamUtils.getString(paramMap.get(Expense.DESCRIPTION)));
            if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Expense.FULL_EDIT_ROLE)) {
                orphanExpense.setExpendDate(ParamUtils.getTimestamp(paramMap.get(Expense.EXPEND_DATE)));
                orphanExpense.setAmount(ParamUtils.getBigDecimal(paramMap.get(Expense.AMOUNT)));
            } else if (DateUtils.isToday(dbExpense.getExpendDate())) {
                orphanExpense.setAmount(ParamUtils.getBigDecimal(paramMap.get(Expense.AMOUNT)));
            }
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!dbExpense.getCategory().equals(orphanExpense.getCategory())) {
            return orphanExpense;
        } else if (!StringUtils.equals(dbExpense.getTitle(), orphanExpense.getTitle())) {
            return orphanExpense;
        } else if (!StringUtils.equals(dbExpense.getDescription(), orphanExpense.getDescription())) {
            return orphanExpense;
        } else if (DateUtils.compare(dbExpense.getExpendDate(), orphanExpense.getExpendDate()) != 0) {
            return orphanExpense;
        } else if (!dbExpense.getAmount().equals(orphanExpense.getAmount())) {
            return orphanExpense;
        } else {
            return null;
        }
    }

    private Expense getDeleteExpense(Integer id) {
        Expense dbExpense = this.queryById(id);
        dbExpense.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbExpense.setModifier(pvgInfo.getLoginId());
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Expense.FULL_EDIT_ROLE)) {
            return dbExpense;
        } else if (DateUtils.isToday(dbExpense.getExpendDate())) {
            return dbExpense;
        }
        throw new RuntimeException("无权限删除此条数据");
    }

    @Override
    public ExpenseWrap queryForWrap(Integer id) {
        ExpenseWrapHelper helper = new ExpenseWrapHelper();
        helper.addWrap(this.queryById(id));
        return helper.getWrappedForObject();
    }

    private Expense queryById(Integer id) {
        if (id == null || id == 0) return null;
        Expense dbExpense = expenseService.queryById(id);
        if (dbExpense == null) {
            log.error("can't find expense with id[" + id + "]");
            return null;
        }
        return dbExpense;
    }

    @Override
    public void save() {
        Expense expense = getNewExpense();
        expenseService.save(expense);
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Expense expense = getUpdateExpense(id);
        if (expense != null) {
            expenseService.update(expense);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Expense expense = getDeleteExpense(id);
        expenseService.delete(expense);
    }

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
