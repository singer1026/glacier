package com.qinyin.athene.service;

import com.qinyin.athene.model.AppResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResourceService extends BaseService<AppResource, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppResource> queryCommonList(String category);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppResource> queryCompanyList(String category);
}
