package com.qinyin.finance.proxy;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccountStatisticsProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Integer queryForCount(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Object> queryForList(Map<String, Object> paramMap);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Map<String, Object> computeToday();

    @Transactional(rollbackFor = Exception.class)
    public void rebuildAll();

    @Transactional(rollbackFor = Exception.class)
    public void rebuildAll(Integer companyId);

    @Transactional(rollbackFor = Exception.class)
    public void buildLastDays(Integer companyId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List computeExpenseCategoryAmountSum(Date start, Date end);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List computeIncomeCategoryAmountSum(Date start, Date end);
}
