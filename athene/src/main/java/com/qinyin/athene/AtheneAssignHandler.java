/**
 * @author zhaolie
 * @create-time 2010-11-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene;

import com.qinyin.athene.annotation.*;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.execution.Execution;
import com.qinyin.athene.execution.impl.CommonExecution;
import com.qinyin.athene.execution.impl.QueryExecution;
import com.qinyin.athene.singleton.JacksonJsonMapper;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class AtheneAssignHandler {
    private Map<String, AtheneMappingBean> urlMappingMap;
    private Map<String, Object> queryableMap;
    public static Logger log = LoggerFactory.getLogger(AtheneAssignHandler.class);
    private final String args = "args";

    public void init(List<Object> beans) throws Exception {
        urlMappingMap = new HashMap<String, AtheneMappingBean>();
        queryableMap = new HashMap<String, Object>();
        for (Object bean : beans) {
            if (bean instanceof Queryable) {
                QueryableMapping queryableMapping = bean.getClass().getAnnotation(QueryableMapping.class);
                if (queryableMapping == null) {
                    log.error(bean.getClass().getName() + " QueryableMapping is not exist");
                    throw new Exception();
                }
                String url = queryableMapping.value();
                if (StringUtils.isBlank(url)) {
                    log.error(bean.getClass().getName() + " QueryableMapping value is not exist");
                    throw new Exception();
                }
                if (queryableMap.get(url) != null) {
                    log.error("can not define same url[" + url + "]");
                    throw new Exception();
                }
                queryableMap.put(url, bean);
            }
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                if (urlMapping != null) {
                    String url = urlMapping.value();
                    if (urlMappingMap.get(url) != null) {
                        log.error("can not define same url[" + url + "]");
                        throw new Exception();
                    }
                    String urlMappingType = urlMapping.type();
                    Class<?> returnClass = method.getReturnType();
                    if (StringUtils.equals(urlMappingType, ReturnType.FREEMARKER)) {
                        String className = returnClass.getName();
                        if ((!className.equals("java.util.Map")) && (!className.equals("java.util.HashMap"))) {
                            log.error("url[" + url + "] must define java.util.Map or java.util.HashMap returnType");
                            throw new Exception();
                        }
                    }
                    AtheneMappingBean mappingBean = new AtheneMappingBean();
                    mappingBean.setBean(bean);
                    mappingBean.setMethod(method);
                    mappingBean.setUrlMapping(urlMapping);
                    mappingBean.setParameterTypes(method.getParameterTypes());
                    mappingBean.setParameterAnnotations(method.getParameterAnnotations());
                    urlMappingMap.put(url, mappingBean);
                }
            }
        }
        Set<Map.Entry<String, AtheneMappingBean>> urlMappingMapSet = urlMappingMap.entrySet();
        for (Map.Entry<String, AtheneMappingBean> urlMappingMapEntry : urlMappingMapSet) {
            String urlMappingMapKey = urlMappingMapEntry.getKey();
            Set<Map.Entry<String, Object>> queryableMapSet = queryableMap.entrySet();
            for (Map.Entry<String, Object> queryableMapEntry : queryableMapSet) {
                if (StringUtils.equals(urlMappingMapKey, queryableMapEntry.getKey())) {
                    log.error("can not define same url[" + urlMappingMapKey + "]");
                    throw new Exception();
                }
            }
        }
    }

    public AtheneMappingBean getAtheneMappingBean(HttpServletRequest req) {
        return urlMappingMap.get(HttpUtils.getRelativeUrl(req));
    }

    public Object getQueryableMappingBean(HttpServletRequest req) {
        return queryableMap.get(HttpUtils.getRelativeUrl(req));
    }

    public Execution getQueryExecution(HttpServletRequest req, Object bean) throws IOException {
        return new QueryExecution(req, (Queryable) bean, getParamMap(req));
    }

    public Execution getCommonExecution(HttpServletRequest req, HttpServletResponse resp, AtheneMappingBean mappingBean) throws Exception {
        return new CommonExecution(mappingBean, buildArguments(req, resp, mappingBean));
    }

    private Object[] buildArguments(HttpServletRequest req, HttpServletResponse resp, AtheneMappingBean mappingBean) throws Exception {
        Annotation[][] paramAnns = mappingBean.getParameterAnnotations();
        Class[] parameterTypes = mappingBean.getParameterTypes();
        Map<String, Object> paramMap = getParamMap(req);
        Map<String, Object> requestMap = getRequestMap(req);
        //获取参数类型的数组
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            //获取参数的类型
            String className = parameterTypes[i].getName();
            //获取单个参数的Annotation数组
            Annotation[] annotations = paramAnns[i];
            for (Annotation annotation : annotations) {
                if (Args.class.isInstance(annotation)) {
                    if (className.equals("java.util.Map") || className.equals("java.util.HashMap")) {
                        arguments[i] = paramMap;
                    }
                    break;
                }
                if (Request.class.isInstance(annotation)) {
                    arguments[i] = req;
                    break;
                }
                if (RequestMap.class.isInstance(annotation)) {
                    arguments[i] = requestMap;
                    break;
                }
                if (Response.class.isInstance(annotation)) {
                    arguments[i] = resp;
                    break;
                }
            }
        }
        return arguments;
    }

    private Map<String, Object> getParamMap(HttpServletRequest req) throws IOException {
        String argsString = req.getParameter(args);
        if (StringUtils.isBlank(argsString)) argsString = "{}";
        Map<String, Object> paramMap = (Map<String, Object>) (JacksonJsonMapper.getInstance().readValue(argsString, HashMap.class));
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        AtheneUtils.setParamMap(paramMap);
        return paramMap;
    }

    private Map<String, Object> getRequestMap(HttpServletRequest req) {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        Map requestMap = req.getParameterMap();
        Iterator<Map.Entry<String, Object>> iterator = requestMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            String[] value = (String[]) entry.getValue();
            if (value.length == 1) {
                hashMap.put(key, value[0]);
            } else {
                hashMap.put(key, value);
            }
        }
        AtheneUtils.setRequestMap(hashMap);
        return hashMap;
    }
}
