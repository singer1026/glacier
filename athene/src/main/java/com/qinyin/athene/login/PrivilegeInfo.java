/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

public class PrivilegeInfo {
    private String LoginId;
    private String sessionId;
    private Integer companyId;

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
