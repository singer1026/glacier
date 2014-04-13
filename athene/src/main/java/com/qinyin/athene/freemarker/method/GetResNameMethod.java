/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-18
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.method;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.constant.BeanConstants;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetResNameMethod implements TemplateMethodModel {
    public static Logger log = LoggerFactory.getLogger(GetResNameMethod.class);

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        try {
            if (arguments == null || arguments.size() != 3) return StringUtils.EMPTY;
            ResourceCacheBean resourceBean = null;
            String type = (String) arguments.get(0);
            String category = (String) arguments.get(1);
            String value = (String) arguments.get(2);
            ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean(BeanConstants.RESOURCE_PROXY);
            if (StringUtils.equals(type, ModelConstants.RESOURCE_TYPE_COMMON)) {
                resourceBean = resourceProxy.getCommonBean(category);
            } else if (StringUtils.equals(type, ModelConstants.RESOURCE_TYPE_COMPANY)) {
                resourceBean = resourceProxy.getCompanyBean(category);
            } else {
                return StringUtils.EMPTY;
            }
            return resourceBean != null ? resourceBean.getName(value) : StringUtils.EMPTY;
        } catch (Exception e) {
            log.error("catch exception", e);
            throw new TemplateModelException();
        }
    }
}
