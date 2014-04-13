package com.qinyin.athene.service;


import com.qinyin.athene.model.AppMenu;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuService extends BaseService<AppMenu, String> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<AppMenu> queryAllListByType(String type);
}
