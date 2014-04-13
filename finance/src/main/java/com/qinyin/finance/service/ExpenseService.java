package com.qinyin.finance.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.finance.model.Expense;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface ExpenseService extends BaseService<Expense, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Expense queryFirstByCompanyId(Integer companyId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public BigDecimal computeAmountSumByExpendDate(Integer companyId, Date expendDate);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public BigDecimal computeAmountSum(Map<String, Object> condition);
}
