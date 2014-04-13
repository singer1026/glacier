/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.MenuCacheBean;
import com.qinyin.athene.cache.store.MenuStore;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.model.AppMenu;
import com.qinyin.athene.proxy.MenuProxy;
import com.qinyin.athene.service.MenuRoleService;
import com.qinyin.athene.service.MenuService;
import com.qinyin.athene.util.ParamUtils;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MenuProxyImpl implements MenuProxy {
    public static Logger log = LoggerFactory.getLogger(MenuProxyImpl.class);
    private MenuService menuService;
    private MenuStore menuStore;
    private MenuRoleService menuRoleService;

    @Override
    public MenuCacheBean queryByName(String name) {
        if (StringUtils.isBlank(name)) return null;
        MenuCacheBean bean = menuStore.get(name);
        if (bean == null) {
            AppMenu dbAppMenu = menuService.queryById(name);
            if (dbAppMenu == null) {
                log.error("can't find subject with name[" + name + "]");
                return null;
            }
            bean = new MenuCacheBean(dbAppMenu);
            List<String> roleList = menuRoleService.queryRoleListByMenuName(bean.getName());
            bean.setRoleList(roleList);
            menuStore.put(name, bean);
        }
        return bean;
    }

    @Override
    public List<MenuCacheBean> queryDesktopMenuList() {
        List<MenuCacheBean> rtnList = new ArrayList<MenuCacheBean>();
        List list = menuStore.getAllListByType(ModelConstants.MENU_TYPE_DESKTOP);
        if (list == null || list.size() == 0) {
            List<AppMenu> menuList = menuService.queryAllListByType(ModelConstants.MENU_TYPE_DESKTOP);
            if (menuList == null || menuList.size() == 0) {
                log.error("can't find any menu with type[" + ModelConstants.MENU_TYPE_DESKTOP + "]");
                return rtnList;
            }
            list = (List) CollectionUtils.collect(menuList, new BeanToPropertyValueTransformer(AppMenu.NAME));
            menuStore.putAllList(ModelConstants.MENU_TYPE_DESKTOP, list);
        }
        for (Object obj : list) {
            String name = ParamUtils.getString(obj);
            MenuCacheBean menuCacheBean = this.queryByName(name);
            if (menuCacheBean != null) {
                rtnList.add(menuCacheBean);
            }
        }
        return rtnList;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public void setMenuStore(MenuStore menuStore) {
        this.menuStore = menuStore;
    }

    public void setMenuRoleService(MenuRoleService menuRoleService) {
        this.menuRoleService = menuRoleService;
    }
}
