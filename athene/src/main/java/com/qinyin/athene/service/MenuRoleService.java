package com.qinyin.athene.service;


import com.qinyin.athene.model.AppMenuRole;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuRoleService extends BaseService<AppMenuRole, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<String> queryRoleListByMenuName(String menuName);

}
