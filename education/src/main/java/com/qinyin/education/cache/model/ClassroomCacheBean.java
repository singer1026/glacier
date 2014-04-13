/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.model;

import com.qinyin.education.model.Classroom;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassroomCacheBean extends Classroom {
    public static Logger log = LoggerFactory.getLogger(ClassroomCacheBean.class);

    public ClassroomCacheBean(Classroom classroom) {
        try {
            PropertyUtils.copyProperties(this, classroom);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public ClassroomCacheBean() {
    }
}
