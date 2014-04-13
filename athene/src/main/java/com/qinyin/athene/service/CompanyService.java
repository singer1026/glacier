package com.qinyin.athene.service;


import com.qinyin.athene.model.AppCompany;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyService extends BaseService<AppCompany, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppCompany> queryAllList();
}
