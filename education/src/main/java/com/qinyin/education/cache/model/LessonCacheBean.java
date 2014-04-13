/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.cache.model;

import com.qinyin.education.model.Lesson;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.model.LessonTeacher;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LessonCacheBean extends Lesson {
    public static Logger log = LoggerFactory.getLogger(LessonCacheBean.class);
    private List<LessonPlan> lessonPlanList = new ArrayList<LessonPlan>();
    private List<LessonTeacher> lessonTeacherList = new ArrayList<LessonTeacher>();
    private List<LessonStudent> lessonStudentList = new ArrayList<LessonStudent>();

    public LessonCacheBean(Lesson lesson) {
        try {
            PropertyUtils.copyProperties(this, lesson);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public LessonCacheBean() {
    }

    public List<LessonPlan> getLessonPlanList() {
        return lessonPlanList;
    }

    public void setLessonPlanList(List<LessonPlan> lessonPlanList) {
        this.lessonPlanList = lessonPlanList;
    }

    public List<LessonTeacher> getLessonTeacherList() {
        return lessonTeacherList;
    }

    public void setLessonTeacherList(List<LessonTeacher> lessonTeacherList) {
        this.lessonTeacherList = lessonTeacherList;
    }

    public List<LessonStudent> getLessonStudentList() {
        return lessonStudentList;
    }

    public void setLessonStudentList(List<LessonStudent> lessonStudentList) {
        this.lessonStudentList = lessonStudentList;
    }
}
