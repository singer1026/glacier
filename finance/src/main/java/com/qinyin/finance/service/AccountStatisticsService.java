package com.qinyin.finance.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.finance.model.AccountStatistics;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface AccountStatisticsService extends BaseService<AccountStatistics, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public AccountStatistics queryLastByCompanyId(Integer companyId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public AccountStatistics queryByStatisticsDate(Date statisticsDate);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void deleteAllByCompanyId(Integer companyId);
}
