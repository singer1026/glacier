/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.service.UserService;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.validation.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserResource {
    public static Logger log = LoggerFactory.getLogger(UserResource.class);
    private UserService userService;
    private UserProxy userProxy;
    private UserValidation userValidation;
    private PrivilegeInfo pvgInfo;

    @UrlMapping(value = "/userOwnEdit", type = ReturnType.FREEMARKER, url = "app/userOwnEdit.ftl")
    public Map<String, Object> editOwn(@Args Map<String, Object> paramMap) {
        UserCacheBean bean = userProxy.queryByLoginId(pvgInfo.getLoginId());
        paramMap.put("user", bean);
        return paramMap;
    }

    @UrlMapping(value = "/userOwnUpdate", type = ReturnType.JSON)
    public DataInfoBean updateOwn() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            info = userValidation.ownValidate();
            if (info.hasError()) return info;
            userProxy.updateOwn();
        } catch (Exception e) {
            log.error("update own user error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/passwordEdit", type = ReturnType.FREEMARKER, url = "app/passwordEdit.ftl")
    public Map<String, Object> editPassword(@Args Map<String, Object> paramMap) {
        return paramMap;
    }

    @UrlMapping(value = "/passwordUpdate", type = ReturnType.JSON)
    public DataInfoBean updatePassword() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            info = userValidation.passwordValidate();
            if (info.hasError()) return info;
            userProxy.updatePassword();
        } catch (Exception e) {
            log.error("update password error", e);
            info.setException(e);
        }
        return info;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
