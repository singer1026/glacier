/**
 * @author zhaolie
 * @create-time 2010-11-22
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene;

import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.container.AtheneBeanFactory;
import com.qinyin.athene.exception.NullBeanException;
import com.qinyin.athene.execution.Execution;
import com.qinyin.athene.freemarker.method.FreeMakerMethodFactory;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.login.SessionCleaner;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.*;
import com.qinyin.athene.view.ViewFactory;
import com.qinyin.athene.view.ViewFactoryHolder;
import freemarker.core.InvalidReferenceException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

public class AtheneServlet extends HttpServlet {
    public static Logger log = LoggerFactory.getLogger(AtheneServlet.class);
    private FreeMakerMethodFactory freeMakerMethodFactory;
    private StaticResourceHandler staticResourceHandler;
    private AtheneAssignHandler atheneAssignHandler;
    private ViewFactory viewFactory;
    private List<Object> beans;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        try {
            super.init(config);
            SystemConfiguration.getInstance().reloadConfig(config.getServletContext());
            AtheneBeanFactory atheneBeanFactory = AtheneFactoryUtils.createContainerFactory();
            atheneBeanFactory.init(config);
            BeanFactoryHolder.setAtheneBeanFactory(atheneBeanFactory);
            beans = atheneBeanFactory.getAllBeans();
            staticResourceHandler = new StaticResourceHandler(config);
            atheneAssignHandler = new AtheneAssignHandler();
            atheneAssignHandler.init(beans);
            viewFactory = new ViewFactory();
            viewFactory.init(config.getServletContext());
            ViewFactoryHolder.setViewFactory(viewFactory);
            freeMakerMethodFactory = new FreeMakerMethodFactory();
            freeMakerMethodFactory.init();
            SessionCleaner sessionCleaner = new SessionCleaner();
            Thread sessionCleanerThread = new Thread(sessionCleaner);
            sessionCleanerThread.start();
        } catch (Exception e) {
            log.error("catch exception ", e);
            System.exit(0);
        }
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AtheneUtils.setServletContext(this.getServletContext());
            String url = HttpUtils.getRelativeUrl(req);
            if (url.contains(".")) {
                staticResourceHandler.handle(req, resp);
                return;
            }
            String query = req.getParameter("query");
            if (StringUtils.equals(query, "true")) {
                Object bean = atheneAssignHandler.getQueryableMappingBean(req);
                if (bean == null) {
                    throw new NullBeanException();
                }
                Execution execution = atheneAssignHandler.getQueryExecution(req, bean);
//                Thread.sleep(200);
                viewFactory.getJsonView().render(req, resp, execution.excute(), null);
                return;
            }
            AtheneMappingBean mappingBean = atheneAssignHandler.getAtheneMappingBean(req);
            if (mappingBean == null) {
                throw new NullBeanException();
            }
            Object result = atheneAssignHandler.getCommonExecution(req, resp, mappingBean).excute();
            if (StringUtils.equals(ReturnType.FREEMARKER, mappingBean.getUrlMapping().type())) {
                String returnUrl = mappingBean.getUrlMapping().url();
                Map<String, Object> data = FreeMarkerUtils.getInitMap(req);
                if (result != null) {
                    data.putAll((Map<String, Object>) result);
                }
                PrivilegeInfo pvgInfo = (PrivilegeInfo) BeanFactoryHolder.getBean("pvgInfo");
                String loginId = pvgInfo.getLoginId();
                data.put("hasBaseRole", freeMakerMethodFactory.getHasBaseRoleMethod());
                data.put("hasRole", freeMakerMethodFactory.getHasRoleMethod());
                data.put("getResName", freeMakerMethodFactory.getGetResNameMethod());
//                Thread.sleep(200);
                viewFactory.getFreeMarkerView().render(req, resp, data, returnUrl);
                return;
            }
            if (StringUtils.equals(ReturnType.JSON, mappingBean.getUrlMapping().type())) {
//                Thread.sleep(200);
                viewFactory.getJsonView().render(req, resp, result, null);
                return;
            }
            if (StringUtils.equals(ReturnType.JSP, mappingBean.getUrlMapping().type())) {
                String returnUrl = mappingBean.getUrlMapping().url();
                viewFactory.getJspView().render(req, resp, result, returnUrl);
                return;
            }
        } catch (NullBeanException e) {
            log.error("can not find urlMapping[" + req.getRequestURI() + "]", e);
            renderException(req, resp, e);
        } catch (SocketException e) {
            renderException(req, resp, e);
        } catch (InvalidReferenceException e) {
            renderException(req, resp, e);
        } catch (Exception e) {
            log.error("catch exception ", e);
            renderException(req, resp, e);
        } finally {
            AtheneUtils.clear();
        }
    }

    public void renderException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            if (StringUtils.contains(req.getHeader("Accept"), "json")) {
                viewFactory.getJsonView().render(req, resp, new DataInfoBean(e), null);
            } else {
                Map<String, Object> data = FreeMarkerUtils.getInitMap(req);
                data.put("exception", CommonUtils.getExceptionDetail(e));
                String returnUrl = SystemConfiguration.getInstance().getProperty("exception.page");
                viewFactory.getFreeMarkerView().render(req, resp, data, returnUrl);
            }
        } catch (Exception ex) {
            log.error("catch exception ", ex);
        }
    }
}
