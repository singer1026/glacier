/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-18
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.method;

import com.qinyin.athene.constant.BeanConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.PermissionUtils;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HasBaseRoleMethod implements TemplateMethodModel {
    public static Logger log = LoggerFactory.getLogger(HasBaseRoleMethod.class);

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        try {
            if (arguments == null || arguments.size() == 0) return false;
            Privilege pvg = (Privilege) BeanFactoryHolder.getBean(BeanConstants.PVG);
            PrivilegeInfo pvgInfo = (PrivilegeInfo) BeanFactoryHolder.getBean(BeanConstants.PVG_INFO);
            List<String> userBaseRoleList = pvg.getBaseRoleListByLoginId(pvgInfo.getLoginId());
            return PermissionUtils.contain(arguments, userBaseRoleList);
        } catch (Exception e) {
            log.error("catch exception", e);
            throw new TemplateModelException();
        }
    }
}
