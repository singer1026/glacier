package com.qinyin.athene.service;


import com.qinyin.athene.model.AppUserRoleOrg;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleOrgService extends BaseService<AppUserRoleOrg, String> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppUserRoleOrg> queryListByLoginId(String loginId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppUserRoleOrg> queryListByUserRole(String roleName);
}
