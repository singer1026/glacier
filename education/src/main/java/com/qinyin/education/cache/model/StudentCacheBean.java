/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.model;

import com.qinyin.education.model.Student;
import com.qinyin.education.model.Subject;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StudentCacheBean extends Student {
    public static Logger log = LoggerFactory.getLogger(StudentCacheBean.class);
    private List subjectList = new ArrayList();

    public StudentCacheBean(Student student) {
        try {
            PropertyUtils.copyProperties(this, student);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public StudentCacheBean() {
    }

    public void addSubject(Subject subject) {
        this.subjectList.add(subject);
    }

    public List getSubjectList() {
        return subjectList;
    }

}
