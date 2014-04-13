/**
 * @author zhaolie
 * @create-time 2010-7-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import com.qinyin.athene.cache.model.UrlCacheBean;
import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.constant.BeanConstants;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.proxy.UrlProxy;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.FreeMarkerUtils;
import com.qinyin.athene.util.HttpUtils;
import com.qinyin.athene.util.PermissionUtils;
import com.qinyin.athene.view.ViewFactoryHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SecurityFilter implements Filter {
    public static Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            String url = HttpUtils.getRelativeUrl(req);
            //如果是请求文件直接通过
            if (url.contains(".")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            SessionDo sessionDo = null;
            LoginDo loginDo = getLoginDo(req);
            if (loginDo != null) {
                sessionDo = getSession(loginDo);
            }
            Privilege pvg = (Privilege) BeanFactoryHolder.getBean(BeanConstants.PVG);
            UrlProxy urlProxy = (UrlProxy) BeanFactoryHolder.getBean(BeanConstants.URL_PROXY);
            UrlCacheBean urlBean = urlProxy.queryByName(url);
            if (urlBean == null) {
                render404(req, resp);
                return;
            }
            if (sessionDo == null) {
                if (StringUtils.equals(urlBean.getIsAnonymous(), ModelConstants.YES)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    renderLogin(req, resp);
                    return;
                }
            } else {
                if (StringUtils.equals(url, SystemConfiguration.getInstance().getProperty("login.url"))
                        || StringUtils.equals(url, SystemConfiguration.getInstance().getProperty("do.login.url"))) {
                    renderIndex(req, resp);
                    return;
                } else if (StringUtils.equals(urlBean.getIsAnonymous(), ModelConstants.YES)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else if (checkPermission(url, sessionDo)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    renderNoPermission(req, resp);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("catch exception", e);
        }
    }

    private LoginDo getLoginDo(HttpServletRequest req) throws Exception {
        CookieUtil cookieUtil = (CookieUtil) BeanFactoryHolder.getBean("cookieUtil");
        return cookieUtil.getCookie(req);
    }

    private SessionDo getSession(LoginDo loginDo) {
        SessionFactory sessionFactory = (SessionFactory) BeanFactoryHolder.getBean("sessionFactory");
        SessionDo sessionDo = sessionFactory.getSession(loginDo.getSessionId());
        if (sessionDo != null) {
            //如果session有效，重新设置超时时间
            String sessionExpire = SystemConfiguration.getInstance().getProperty("session.expire");
            Long expire = (new Date()).getTime() + Long.valueOf(sessionExpire);
            sessionDo.setExpire(expire);
        }
        return sessionDo;
    }

    private boolean checkPermission(String url, SessionDo sessionDo) {
        Privilege pvg = (Privilege) BeanFactoryHolder.getBean(BeanConstants.PVG);
        PrivilegeInfo pvgInfo = (PrivilegeInfo) BeanFactoryHolder.getBean(BeanConstants.PVG_INFO);
        UrlProxy urlProxy = (UrlProxy) BeanFactoryHolder.getBean(BeanConstants.URL_PROXY);
        UserProxy userProxy = (UserProxy) BeanFactoryHolder.getBean(BeanConstants.USER_PROXY);
        String loginId = sessionDo.getLoginId();
        UrlCacheBean urlBean = urlProxy.queryByName(url);
        UserCacheBean userBean = userProxy.queryByLoginId(loginId);
        if (urlBean == null || userBean == null) {
            return false;
        }
        if (StringUtils.equals(urlBean.getHasPermission(), ModelConstants.NO)) {
            pvgInfo.setLoginId(loginId);
            pvgInfo.setSessionId(sessionDo.getSessionId());
            pvgInfo.setCompanyId(userBean.getCompanyId());
            return true;
        } else {
            List<String> allowedBaseRoleList = urlBean.getRoleList();
            List<String> baseRoleList = pvg.getBaseRoleListByLoginId(loginId);
            if (PermissionUtils.contain(allowedBaseRoleList, baseRoleList)) {
                pvgInfo.setLoginId(loginId);
                pvgInfo.setSessionId(sessionDo.getSessionId());
                pvgInfo.setCompanyId(userBean.getCompanyId());
                return true;
            }
        }
        return false;
    }

    private void renderNoPermission(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String accept = req.getHeader("Accept");
        if (StringUtils.contains(accept, "json")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            Map<String, Object> data = FreeMarkerUtils.getInitMap(req);
            String noPermissionPage = SystemConfiguration.getInstance().getProperty("noPermission.page");
            ViewFactoryHolder.getFreeMarkerView().render(req, resp, data, noPermissionPage);
        }
    }

    private void render404(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String accept = req.getHeader("Accept");
        if (StringUtils.contains(accept, "json")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            Map<String, Object> data = FreeMarkerUtils.getInitMap(req);
            String noPermissionPage = SystemConfiguration.getInstance().getProperty("404.page");
            ViewFactoryHolder.getFreeMarkerView().render(req, resp, data, noPermissionPage);
        }
    }

    private void renderLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String xRequestedWith = req.getHeader("X-Requested-With");
        String loginUrl = SystemConfiguration.getInstance().getProperty("login.url");
        if (StringUtils.contains(xRequestedWith, "XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            String url = HttpUtils.getRelativeUrl(req);
            if (!StringUtils.equals(url, loginUrl)) {
                String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
                resp.sendRedirect(basePath + loginUrl);
            }
        }
    }

    private void renderIndex(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String xRequestedWith = req.getHeader("X-Requested-With");
        if (StringUtils.contains(xRequestedWith, "XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
            resp.sendRedirect(basePath + "/");
        }
    }

    public void destroy() {
    }
}
