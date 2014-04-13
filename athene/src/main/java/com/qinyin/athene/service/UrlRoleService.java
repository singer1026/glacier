package com.qinyin.athene.service;


import com.qinyin.athene.model.AppUrlRole;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UrlRoleService extends BaseService<AppUrlRole, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<String> queryRoleListByUrlName(String urlName);
}
