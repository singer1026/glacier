/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.model;

import com.qinyin.education.model.Subject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubjectCacheBean extends Subject {
    public static Logger log = LoggerFactory.getLogger(SubjectCacheBean.class);

    public SubjectCacheBean(Subject subject) {
        try {
            PropertyUtils.copyProperties(this, subject);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public SubjectCacheBean() {
    }

    public Subject toSubject() {
        Subject subject = new Subject();
        try {
            BeanUtils.copyProperties(subject, this);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
        return subject;
    }
}
