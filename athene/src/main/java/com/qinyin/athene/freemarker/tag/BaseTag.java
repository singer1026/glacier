/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag;

import com.qinyin.athene.constant.BeanConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.PermissionUtils;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class BaseTag implements TemplateDirectiveModel {

    protected String getString(Map params, String key) {
        Object _value = params.get(key);
        if (_value != null && _value instanceof SimpleScalar) {
            SimpleScalar simpleScalar = (SimpleScalar) _value;
            String value = simpleScalar.getAsString();
            return StringUtils.isNotBlank(value) ? value : null;
        } else {
            return null;
        }
    }

    protected Integer getInteger(Map params, String key) {
        String _value = this.getString(params, key);
        try {
            return Integer.valueOf(_value);
        } catch (Exception e) {
        }
        return null;
    }

    protected boolean getBoolean(Map params, String key, boolean defaultValue) {
        String _value = this.getString(params, key);
        if (_value != null) {
            return StringUtils.equals(_value, "true") ? true : false;
        }
        return defaultValue;
    }

    protected List getList(Map params, String key) throws TemplateModelException {
        Object _value = params.get(key);
        if (_value != null && _value instanceof SimpleSequence) {
            return ((SimpleSequence) _value).toList();
        }
        return null;
    }

    protected boolean checkPermission(Map params) {
        String baseRole = getString(params, "baseRole");
        String role = getString(params, "role");
        if (role == null && baseRole == null) return true;
        Privilege pvg = (Privilege) BeanFactoryHolder.getBean(BeanConstants.PVG);
        PrivilegeInfo pvgInfo = (PrivilegeInfo) BeanFactoryHolder.getBean(BeanConstants.PVG_INFO);
        String loginId = pvgInfo.getLoginId();
        if (baseRole != null) {
            List<String> allowedBaseRoleList = Arrays.asList(baseRole.split(","));
            List<String> userBaseRoleList = pvg.getBaseRoleListByLoginId(loginId);
            return PermissionUtils.contain(allowedBaseRoleList, userBaseRoleList);
        }
        if (role != null) {
            List<String> allowedRoleList = Arrays.asList(role.split(","));
            List<String> userRoleList = pvg.getUserRoleListByLoginId(loginId);
            return PermissionUtils.contain(allowedRoleList, userRoleList);
        }
        return false;
    }
}