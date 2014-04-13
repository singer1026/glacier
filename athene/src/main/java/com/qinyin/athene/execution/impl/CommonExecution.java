/**
 * @author zhaolie
 * @create-time 2010-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.execution.impl;

import com.qinyin.athene.AtheneMappingBean;
import com.qinyin.athene.execution.Execution;

import java.lang.reflect.Method;

public class CommonExecution implements Execution {
    private Object bean;
    private Method method;
    private Object[] arguments;

    public CommonExecution() {
    }

    public CommonExecution(AtheneMappingBean mappingBean, Object[] arguments) {
        this.bean = mappingBean.getBean();
        this.method = mappingBean.getMethod();
        this.arguments = arguments;
    }

    public Object excute() throws Exception {
        return method.invoke(bean, arguments);
    }

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

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
