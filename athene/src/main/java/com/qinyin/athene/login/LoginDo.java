/**
 * @author zhaolie
 * @create-time 2010-7-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import java.io.Serializable;

public class LoginDo implements Serializable {
    private String sessionId;
    private String loginId;
    private String password;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
