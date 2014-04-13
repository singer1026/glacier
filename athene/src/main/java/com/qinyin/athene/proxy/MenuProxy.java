package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.MenuCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MenuProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public MenuCacheBean queryByName(String name);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<MenuCacheBean> queryDesktopMenuList();
}
