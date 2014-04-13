package com.qinyin.athene.service;


import com.qinyin.athene.model.AppRoleBaseRole;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleBaseRoleService extends BaseService<AppRoleBaseRole, String> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<String> queryBaseRoleByUserRole(String roleName);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<String> queryUserRoleByBaseRole(String baseRoleName);

}
