/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppCompany;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyCacheBean extends AppCompany {
    public static Logger log = LoggerFactory.getLogger(CompanyCacheBean.class);

    public CompanyCacheBean(AppCompany appCompany) {
        try {
            PropertyUtils.copyProperties(this, appCompany);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public CompanyCacheBean() {
    }

}
