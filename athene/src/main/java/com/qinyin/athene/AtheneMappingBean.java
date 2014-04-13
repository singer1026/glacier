/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-14
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene;

import com.qinyin.athene.annotation.UrlMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AtheneMappingBean {
    private Object bean;
    private Method method;
    private UrlMapping urlMapping;
    private Class<?>[] parameterTypes;
    private Annotation[][] parameterAnnotations;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(UrlMapping urlMapping) {
        this.urlMapping = urlMapping;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Annotation[][] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(Annotation[][] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }
}
