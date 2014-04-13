/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.impl;

import java.util.Date;

public class LocalCacheBean {
    private Long expire;
    private Long maxAge;
    private String key;
    private Object value;

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
        Long expire = 0L;
        if (maxAge > 0) {
            expire = (new Date()).getTime() + maxAge;
        }
        this.expire = expire;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        if (this.expire != 0) {
            this.setMaxAge(this.maxAge);
        }
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isAvailable() {
        if (this.expire == 0) {
            return true;
        } else {
            Date date = new Date();
            return expire - date.getTime() > 0 ? true : false;
        }
    }
}
