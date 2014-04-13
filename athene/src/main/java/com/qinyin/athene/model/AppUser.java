package com.qinyin.athene.model;

import java.sql.Timestamp;

public class AppUser extends AdvanceModel {
    public static final String GMT_LAST_LOGIN = "gmtLastLogin";
    private Timestamp gmtLastLogin;
    public static final String STATUS = "status";
    private String status;
    public static final String LOGIN_ID = "loginId";
    private String loginId;
    public static final String PASSWORD = "password";
    private String password;
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String TYPE = "type";
    private String type;
    public static final String NAME = "name";
    private String name;
    public static final String SEX = "sex";
    private String sex;
    public static final String ADDRESS = "address";
    private String address;
    public static final String QQ = "qq";
    private String qq;
    public static final String EMAIL = "email";
    private String email;
    public static final String TELEPHONE = "telephone";
    private String telephone;
    public static final String MOBILE = "mobile";
    private String mobile;
    public static final String LAST_LOGIN_IP = "lastLoginIp";
    private String lastLoginIp;

    public static final String SQL_FILE_NAME = "AppUser";

    public Timestamp getGmtLastLogin() {
        return gmtLastLogin;
    }

    public void setGmtLastLogin(Timestamp gmtLastLogin) {
        this.gmtLastLogin = gmtLastLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
}