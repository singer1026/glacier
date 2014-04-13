package com.qinyin.finance.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.finance.model.Income;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface IncomeService extends BaseService<Income, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Income queryFirstByCompanyId(Integer companyId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public BigDecimal computeAmountSumByIncomeDate(Integer companyId, Date incomeDate);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public BigDecimal computeAmountSum(Map<String, Object> condition);
}
