package com.qinyin.athene.service;


import com.qinyin.athene.model.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends BaseService<AppUser, String> {
    @Transactional(rollbackFor = Exception.class)
    public void updateLogin(AppUser appUser);

    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(AppUser appUser);

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(AppUser appUser);
}
