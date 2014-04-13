/**
 * @author zhaolie
 * @create-time 2010-11-26
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.cache.model.MenuCacheBean;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.model.AppMenu;
import com.qinyin.athene.proxy.MenuProxy;
import com.qinyin.athene.singleton.JacksonJsonMapper;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.athene.util.PermissionUtils;
import com.qinyin.athene.view.FreeMarkerView;
import com.qinyin.athene.view.ViewFactoryHolder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonResource {
    private Privilege pvg;
    private PrivilegeInfo pvgInfo;
    private MenuProxy menuProxy;

    @UrlMapping(value = "/", type = ReturnType.FREEMARKER, url = "index.ftl")
    public Map<String, Object> index(@Args Map<String, Object> paramMap) throws Exception {
        Boolean minScript = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("min.script"));
        String scriptVersion = ParamUtils.getString(SystemConfiguration.getInstance().getProperty("script.version"));
        paramMap.put("scriptVersion", scriptVersion);
        paramMap.put("minScript", minScript);
        Boolean logConsole = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("log.console"));
        paramMap.put("logConsole", logConsole);
        List<String> baseRoleList = pvg.getBaseRoleListByLoginId(pvgInfo.getLoginId());
        paramMap.put("baseRoleList", JacksonJsonMapper.getInstance().writeValue(baseRoleList));
        List<String> roleList = pvg.getUserRoleListByLoginId(pvgInfo.getLoginId());
        paramMap.put("roleList", JacksonJsonMapper.getInstance().writeValue(roleList));
        FreeMarkerView view = ViewFactoryHolder.getFreeMarkerView();
        paramMap.put("template_window", view.getFileContent(null, "template/window.ftl").replace("\"", "\\\""));
        paramMap.put("template_message", view.getFileContent(null, "template/message.ftl").replace("\"", "\\\""));
        paramMap.put("template_dialog", view.getFileContent(null, "template/dialog.ftl").replace("\"", "\\\""));
        paramMap.put("template_mask", view.getFileContent(null, "template/mask.ftl").replace("\"", "\\\""));
        paramMap.put("template_desktopMenu", view.getFileContent(null, "template/desktopMenu.ftl").replace("\"", "\\\""));
        paramMap.put("desktopMenus", JacksonJsonMapper.getInstance().writeValue(this.getDesktopMenus()));
        String loginId = pvgInfo.getLoginId();
        String userName = pvg.getUserNameByLoginId(loginId);
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("loginId", loginId);
        userMap.put("name", userName);
        paramMap.put("userInfo", JacksonJsonMapper.getInstance().writeValue(userMap));
        return paramMap;
    }

    private List<AppMenu> getDesktopMenus() {
        List<AppMenu> rtnList = new ArrayList<AppMenu>();
        List<MenuCacheBean> desktopMenuList = menuProxy.queryDesktopMenuList();
        for (MenuCacheBean menuCacheBean : desktopMenuList) {
            if (checkPermission(menuCacheBean)) {
                rtnList.add(menuCacheBean);
            }
        }
        return rtnList;
    }

    private boolean checkPermission(MenuCacheBean menuCacheBean) {
        if (StringUtils.equals(menuCacheBean.getHasPermission(), ModelConstants.NO)) return true;
        List<String> allowedBaseRoleList = menuCacheBean.getRoleList();
        List<String> baseRoleList = pvg.getBaseRoleListByLoginId(pvgInfo.getLoginId());
        if (PermissionUtils.contain(allowedBaseRoleList, baseRoleList)) {
            return true;
        } else {
            return false;
        }
    }

    @UrlMapping(value = "/getLogConsole", type = ReturnType.FREEMARKER, url = "template/logConsole.ftl")
    public Map<String, Object> getLogConsole() {
        return null;
    }

    public void setPvg(Privilege pvg) {
        this.pvg = pvg;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setMenuProxy(MenuProxy menuProxy) {
        this.menuProxy = menuProxy;
    }
}

