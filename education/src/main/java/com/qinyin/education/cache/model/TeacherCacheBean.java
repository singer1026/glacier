/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.model;

import com.qinyin.education.model.Subject;
import com.qinyin.education.model.Teacher;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TeacherCacheBean extends Teacher {
    public static Logger log = LoggerFactory.getLogger(TeacherCacheBean.class);
    private List<Subject> subjectList = new ArrayList<Subject>();

    public TeacherCacheBean(Teacher teacher) {
        try {
            PropertyUtils.copyProperties(this, teacher);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public TeacherCacheBean() {
    }

    public void addSubject(Subject subject) {
        this.subjectList.add(subject);
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

}
