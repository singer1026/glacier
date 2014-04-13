/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.validation.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.util.*;
import com.qinyin.athene.validation.UserValidation;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class UserValidationImpl implements UserValidation {
    private PrivilegeInfo pvgInfo;
    private UserProxy userProxy;
    private JdbcDao jdbcDao;

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        String qq = ParamUtils.getString(paramMap.get("qq"));
        if (qq != null) {
            if (!StringUtils.isNumeric(qq)) {
                info.addValidationError("qq-" + number + "-error", "QQ必须为数字");
            } else if (!ValidationUtils.isQQ(qq)) {
                info.addValidationError("qq-" + number + "-error", "QQ格式不正确");
            }
        }
        String telephone = ParamUtils.getString(paramMap.get("telephone"));
        if (telephone != null) {
            if (!StringUtils.isNumeric(telephone)) {
                info.addValidationError("telephone-" + number + "-error", "固定电话必须为数字");
            } else if (!ValidationUtils.isTelephone(telephone)) {
                info.addValidationError("telephone-" + number + "-error", "固定电话格式不正确");
            }
        }
        String mobile = ParamUtils.getString(paramMap.get("mobile"));
        if (mobile == null) {
            info.addValidationError("mobile-" + number + "-error", "必须输入手机号码");
        } else if (!StringUtils.isNumeric(mobile)) {
            info.addValidationError("mobile-" + number + "-error", "手机号码必须为数字");
        } else if (!ValidationUtils.isMobile(mobile)) {
            info.addValidationError("mobile-" + number + "-error", "手机号码格式不正确");
        }
        String email = ParamUtils.getString(paramMap.get("email"));
        if (email != null) {
            if (!ValidationUtils.isEmail(email)) {
                info.addValidationError("email-" + number + "-error", "邮箱地址格式不正确");
            }
        }
        String address = ParamUtils.getString(paramMap.get("address"));
        if (address != null) {
            if (DefenseUtils.isUnlawfulChar(address)) {
                info.addValidationError("address-" + number + "-error", "地址" + DefenseUtils.UNLAWFUL_CHAR);
            } else if (!ValidationUtils.isAllowedLength(address, 256)) {
                info.addValidationError("address-" + number + "-error", "地址最大为256个字符");
            }
        }
        return info;
    }

    @Override
    public DataInfoBean fullValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = this.simpleValidate();
        String number = ParamUtils.getString(requestMap.get("number"));
        String loginId = ParamUtils.getString(paramMap.get("loginId"));
        if (loginId == null) {
            info.addValidationError("login-id-" + number + "-error", "必须输入登录名");
        } else if (!ValidationUtils.isLoginId(loginId)) {
            info.addValidationError("login-id-" + number + "-error", "登录名格式不正确");
        }
        String name = ParamUtils.getString(paramMap.get("name"));
        if (name == null) {
            info.addValidationError("name-" + number + "-error", "必须输入姓名");
        } else if (!ValidationUtils.isChinese(name)) {
            info.addValidationError("name-" + number + "-error", "姓名必须为中文");
        } else if (!ValidationUtils.isAllowedChinese(name, 4)) {
            info.addValidationError("name-" + number + "-error", "姓名最大为4个汉字");
        }
        String sex = ParamUtils.getString(paramMap.get("sex"));
        if (sex == null) {
            info.addValidationError("sex-" + number + "-error", "必须选择性别");
        }
        return info;
    }

    @Override
    public DataInfoBean saveValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        AppUser appUser = new AppUser();
        appUser.setLoginId(ParamUtils.getString(paramMap.get("loginId")));
        Object dbAppUser = jdbcDao.queryForObject("AppUser", "SELECT", appUser, AppUser.class);
        if (dbAppUser != null) {
            info.addValidationError("login-id-" + number + "-error", "登录名[" + appUser.getLoginId() + "]已经存在");
        }
        return info;
    }

    @Override
    public DataInfoBean ownValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        DataInfoBean info = simpleValidate();
        String loginId = ParamUtils.getString(paramMap.get("loginId"));
        if (!pvgInfo.getLoginId().equals(loginId)) {
            throw new RuntimeException("登录名不是本人");
        }
        return info;
    }

    @Override
    public DataInfoBean passwordValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        String passwordOld = ParamUtils.getString(paramMap.get("passwordOld"));
        String passwordNew = ParamUtils.getString(paramMap.get("passwordNew"));
        String passwordConfirm = ParamUtils.getString(paramMap.get("passwordConfirm"));
        if (passwordOld == null) {
            info.addValidationError("password-old-" + number + "-error", "必须输入原密码");
        }
        if (passwordNew == null) {
            info.addValidationError("password-new-" + number + "-error", "必须输入新密码");
        }
        if (passwordConfirm == null) {
            info.addValidationError("password-confirm-" + number + "-error", "必须输入确认密码");
        }
        if (info.hasError()) return info;
        if (!passwordNew.equals(passwordConfirm)) {
            info.addValidationError("password-confirm-" + number + "-error", "新密码和确认密码不一致");
        } else if (!ValidationUtils.isAllowPassword(passwordNew)) {
            info.addValidationError("password-new-" + number + "-error", "新密码格式不正确");
        }
        if (info.hasError()) return info;
        AppUser appUser = userProxy.queryByLoginId(pvgInfo.getLoginId());
        if (!appUser.getPassword().equals(DigestUtils.md5Hex(passwordOld))) {
            info.addValidationError("password-old-" + number + "-error", "原密码输入错误");
        } else if (passwordOld.equals(passwordNew)) {
            info.addValidationError("password-new-" + number + "-error", "新密码不能和原密码相同");
        }
        return info;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }
}
