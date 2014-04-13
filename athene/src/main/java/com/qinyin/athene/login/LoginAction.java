/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-17
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.Request;
import com.qinyin.athene.annotation.Response;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.cache.store.UserStore;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.service.UserService;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.DigestUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.athene.view.FreeMarkerView;
import com.qinyin.athene.view.ViewFactoryHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Map;

public class LoginAction {
    public static Logger log = LoggerFactory.getLogger(LoginAction.class);
    private UserService userService;
    private UserProxy userProxy;
    private SessionFactory sessionFactory;
    private CookieUtil cookieUtil;
    private PrivilegeInfo pvgInfo;
    private UserStore userStore;

    @UrlMapping(value = "/doLogin", type = ReturnType.JSON)
    public DataInfoBean login(@Args Map<String, Object> paramMap, @Request HttpServletRequest req,
                              @Response HttpServletResponse resp) {
        DataInfoBean info = new DataInfoBean();
        try {
            String loginId = ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID));
            String password = ParamUtils.getString(paramMap.get(AppUser.PASSWORD));
            if (loginId == null) {
                info.addValidationError("login-id-error", "请输入用户名");
            }
            if (password == null) {
                info.addValidationError("password-error", "请输入密码");
            }
            if (info.hasError()) return info;
            UserCacheBean bean = userProxy.queryByLoginId(loginId);
            if (bean == null || bean.getIsDeleted().equals("y")) {
                info.addValidationError("login-id-error", "用户不存在");
                return info;
            }
            String passwordMd5 = DigestUtils.md5Hex(password);
            if (!StringUtils.equals(bean.getPassword(), passwordMd5)) {
                info.addValidationError("password-error", "密码错误");
                return info;
            }
            if (StringUtils.equals(bean.getStatus(), ModelConstants.USER_STATUS_LOCK)) {
                info.addValidationError("login-id-error", "用户已经锁定，请联系管理员");
                return info;
            }
            bean.setGmtLastLogin(new Timestamp(System.currentTimeMillis()));
            bean.setLastLoginIp(req.getRemoteAddr());
            userService.updateLogin(bean);
            userStore.remove(bean.getLoginId());
            SessionDo sessionDo = sessionFactory.createSession();
            LoginDo loginDo = new LoginDo();
            loginDo.setSessionId(sessionDo.getSessionId());
            loginDo.setLoginId(bean.getLoginId());
            loginDo.setPassword(bean.getPassword());
            sessionDo.put(AppUser.LOGIN_ID, loginDo.getLoginId());
            cookieUtil.write2Response(resp, loginDo);
        } catch (Exception e) {
            log.error("login error , paramMap=" + paramMap, e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/login", type = ReturnType.FREEMARKER, url = "login.ftl")
    public Map<String, Object> login(@Args Map<String, Object> paramMap) throws Exception {
        Boolean minScript = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("min.script"));
        String scriptVersion = ParamUtils.getString(SystemConfiguration.getInstance().getProperty("script.version"));
        FreeMarkerView view = ViewFactoryHolder.getFreeMarkerView();
        paramMap.put("template_mask", view.getFileContent(null, "template/mask.ftl").replace("\"", "\\\""));
        paramMap.put("template_dialog", view.getFileContent(null, "template/dialog.ftl").replace("\"", "\\\""));
        paramMap.put("scriptVersion", scriptVersion);
        paramMap.put("minScript", minScript);
        return paramMap;
    }

    @UrlMapping(value = "/loginOut", type = ReturnType.FREEMARKER, url = "loginOut.ftl")
    public Map<String, Object> loginOut(@Args Map<String, Object> paramMap) {
        Boolean minScript = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("min.script"));
        String scriptVersion = ParamUtils.getString(SystemConfiguration.getInstance().getProperty("script.version"));
        paramMap.put("scriptVersion", scriptVersion);
        paramMap.put("minScript", minScript);
        try {
            String sessionId = pvgInfo.getSessionId();
            sessionFactory.removeSession(sessionId);
        } catch (Exception e) {
            log.error("loginOut error", e);
        }
        return paramMap;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setCookieUtil(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
}

