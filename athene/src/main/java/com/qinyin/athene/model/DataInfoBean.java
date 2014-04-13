/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.model;

import com.qinyin.athene.util.CommonUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInfoBean {
    private String message;
    private String exceptionName;
    private String exceptionDesc;
    private Object data;
    private List<Map<String, Object>> validationErrors = new ArrayList<Map<String, Object>>();

    public DataInfoBean() {
    }

    public DataInfoBean(Exception e) {
        this.setException(e);
    }

    /**
     * 返回值
     * 0==成功
     * 1==验证错误
     * 2==乐观锁异常
     * 3==其他异常
     */
    public Integer getReturnCode() {
        if (StringUtils.isNotBlank(exceptionName)) {
            if (StringUtils.equals(exceptionName, "OptimisticLockException")) {
                return 2;
            } else if (StringUtils.equals(exceptionName, "HackerException")) {
                return 4;
            } else {
                return 3;
            }
        } else if (validationErrors.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public Boolean hasError() {
        return getReturnCode() != 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getValidationErrors() {
        return validationErrors;
    }

    public void addValidationError(String id, String message) {
        Map<String, Object> validationError = null;
        for (Map<String, Object> obj : validationErrors) {
            if (StringUtils.equals(obj.get("id").toString(), id)) {
                validationError = obj;
            }
        }
        if (validationError == null) {
            Map<String, Object> newError = new HashMap<String, Object>();
            newError.put("id", id);
            newError.put("message", message);
            this.validationErrors.add(newError);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(validationError.get("message")).append(message);
            validationError.put("message", sb.toString());
        }
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setException(Exception e) {
        this.exceptionName = getRealExceptionName(e);
        this.exceptionDesc = CommonUtils.getExceptionDetail(e);
    }

    private String getRealExceptionName(Exception e) {
        Throwable throwable = e;
        if (e instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) e).getTargetException();
        }
        return throwable.getClass().getSimpleName();
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
