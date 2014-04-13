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
import com.qinyin.finance.model.Income;
import com.qinyin.finance.proxy.IncomeProxy;
import com.qinyin.finance.service.IncomeService;
import com.qinyin.finance.wrap.IncomeWrap;
import com.qinyin.finance.wrap.IncomeWrapHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class IncomeProxyImpl implements IncomeProxy {
    public static Logger log = LoggerFactory.getLogger(IncomeProxyImpl.class);
    private IncomeService incomeService;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return incomeService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        IncomeWrapHelper helper = new IncomeWrapHelper();
        List incomeList = incomeService.queryForList(paramMap);
        for (Object obj : incomeList) {
            helper.addWrap((Income) obj);
        }
        return helper.getWrappedForList();
    }

    private Income getNewIncome() {
        String creator = pvgInfo.getLoginId();
        Integer companyId = pvgInfo.getCompanyId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Income income = new Income();
        income.setGmtCreate(now);
        income.setCreator(creator);
        income.setGmtModify(now);
        income.setModifier(creator);
        income.setIsDeleted(ModelConstants.IS_DELETED_N);
        income.setVersion(ModelConstants.VERSION_INIT);
        income.setCompanyId(pvgInfo.getCompanyId());
        income.setCategory(ParamUtils.getString(paramMap.get(Income.CATEGORY)));
        income.setTitle(ParamUtils.getString(paramMap.get(Income.TITLE)));
        income.setDescription(ParamUtils.getString(paramMap.get(Income.DESCRIPTION)));
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Income.FULL_EDIT_ROLE)) {
            income.setIncomeDate(ParamUtils.getTimestamp(paramMap.get(Income.INCOME_DATE)));
        } else {
            income.setIncomeDate(now);
        }
        income.setAmount(ParamUtils.getBigDecimal(paramMap.get(Income.AMOUNT)));
        return income;
    }

    private Income getUpdateIncome(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Income dbIncome = this.queryById(id);
        Income orphanIncome = null;
        try {
            orphanIncome = (Income) BeanUtils.cloneBean(dbIncome);
            orphanIncome.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanIncome.setModifier(pvgInfo.getLoginId());
            orphanIncome.setCategory(ParamUtils.getString(paramMap.get(Income.CATEGORY)));
            orphanIncome.setTitle(ParamUtils.getString(paramMap.get(Income.TITLE)));
            orphanIncome.setDescription(ParamUtils.getString(paramMap.get(Income.DESCRIPTION)));
            if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Income.FULL_EDIT_ROLE)) {
                orphanIncome.setIncomeDate(ParamUtils.getTimestamp(paramMap.get(Income.INCOME_DATE)));
                orphanIncome.setAmount(ParamUtils.getBigDecimal(paramMap.get(Income.AMOUNT)));
            } else if (DateUtils.isToday(dbIncome.getIncomeDate())) {
                orphanIncome.setAmount(ParamUtils.getBigDecimal(paramMap.get(Income.AMOUNT)));
            }
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!dbIncome.getCategory().equals(orphanIncome.getCategory())) {
            return orphanIncome;
        } else if (!StringUtils.equals(dbIncome.getTitle(), orphanIncome.getTitle())) {
            return orphanIncome;
        } else if (!StringUtils.equals(dbIncome.getDescription(), orphanIncome.getDescription())) {
            return orphanIncome;
        } else if (!dbIncome.getAmount().equals(orphanIncome.getAmount())) {
            return orphanIncome;
        } else if (DateUtils.compare(dbIncome.getIncomeDate(), orphanIncome.getIncomeDate()) != 0) {
            return orphanIncome;
        } else {
            return null;
        }
    }

    private Income getDeleteIncome(Integer id) {
        Income dbIncome = this.queryById(id);
        dbIncome.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbIncome.setModifier(pvgInfo.getLoginId());
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Income.FULL_EDIT_ROLE)) {
            return dbIncome;
        } else if (DateUtils.isToday(dbIncome.getIncomeDate())) {
            return dbIncome;
        }
        throw new RuntimeException("无权限删除此条数据");
    }

    @Override
    public IncomeWrap queryForWrap(Integer id) {
        IncomeWrapHelper helper = new IncomeWrapHelper();
        helper.addWrap(this.queryById(id));
        return helper.getWrappedForObject();
    }

    private Income queryById(Integer id) {
        if (id == null || id == 0) return null;
        Income dbIncome = incomeService.queryById(id);
        if (dbIncome == null) {
            log.error("can't find income with id[" + id + "]");
            return null;
        }
        return dbIncome;
    }

    @Override
    public void save() {
        Income income = getNewIncome();
        incomeService.save(income);
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Income income = getUpdateIncome(id);
        if (income != null) {
            incomeService.update(income);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Income income = getDeleteIncome(id);
        incomeService.delete(income);
    }

    public void setIncomeService(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
