/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-16
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.qinyin.athene.model.DataInfoBean;

import javax.servlet.ServletContext;
import java.util.Map;

public class AtheneUtils {
    private static final ThreadLocal<Map<String, Object>> paramMap = new ThreadLocal<Map<String, Object>>();
    private static final ThreadLocal<Map<String, Object>> requestMap = new ThreadLocal<Map<String, Object>>();
    private static final ThreadLocal<DataInfoBean> info = new ThreadLocal<DataInfoBean>();
    private static final ThreadLocal<ServletContext> servlectContext = new ThreadLocal<ServletContext>();

    public static void setParamMap(Map<String, Object> o) {
        paramMap.set(o);
    }

    public static void setRequestMap(Map<String, Object> o) {
        requestMap.set(o);
    }

    public static void setInfo(DataInfoBean o) {
        info.set(o);
    }

    public static DataInfoBean getInfo() {
        DataInfoBean bean = info.get();
        if (bean == null) {
            bean = new DataInfoBean();
            setInfo(bean);
        }
        return bean;
    }

    public static Map<String, Object> getRequestMap() {
        return requestMap.get();
    }

    public static Map<String, Object> getParamMap() {
        return paramMap.get();
    }

    public static void clear() {
        paramMap.remove();
        requestMap.remove();
        info.remove();
        servlectContext.remove();
    }

    public static void setServletContext(ServletContext servletContext) {
        servlectContext.set(servletContext);
    }

    public static ServletContext getServletContext() {
        return servlectContext.get();
    }

}
