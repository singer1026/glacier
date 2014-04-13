/**
 * @author zhaolie
 * @version 1.0
 * @create-time 2010-7-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SessionDo implements Serializable {
    private String id;
    private Map contextMap = new HashMap();
    private Long expire;

    public SessionDo() {
    }

    public SessionDo(String id, Long expire) {
        this.id = id;
        this.expire = expire;
    }

    public String getSessionId() {
        return id;
    }

    public void setSessionId(String sessionId) {
        this.id = id;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public void put(String key, Object obj) {
        contextMap.put(key, obj);
    }

    public Object get(String key) {
        return contextMap.get(key);
    }

    public String getLoginId() {
        return (String) contextMap.get("loginId");
    }

    public boolean isAvailable(Date date) {
        return expire - date.getTime() > 0 ? true : false;
    }

    public boolean isExpire(Date date) {
        return expire - date.getTime() > 0 ? false : true;
    }

}
