/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.ClassroomCacheBean;
import com.qinyin.education.manager.StaticResourceManager;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.proxy.ClassroomProxy;
import com.qinyin.education.util.LessonUtils;

import java.util.ArrayList;
import java.util.List;

public class LessonPlanWrapHelper {
    private List lessonPlanWrapList = new ArrayList();
    private ClassroomProxy classroomProxy;
    private ResourceCacheBean dayOfWeek;
    private StaticResourceManager staticResourceManager;
    private ResourceCacheBean lessonFrequency;

    public LessonPlanWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        classroomProxy = (ClassroomProxy) BeanFactoryHolder.getBean("classroomProxy");
        staticResourceManager = (StaticResourceManager) BeanFactoryHolder.getBean("staticResourceManager");
        dayOfWeek = resourceProxy.getCommonBean("dayOfWeek");
        lessonFrequency = resourceProxy.getCompanyBean("lessonFrequency");
    }

    public void addWrap(LessonPlan lessonPlan) {
        if (lessonPlan == null) return;
        lessonPlanWrapList.add(new LessonPlanWrap(lessonPlan));
    }

    public List getWrappedForList() {
        for (Object obj : lessonPlanWrapList) {
            LessonPlanWrap wrap = (LessonPlanWrap) obj;
            wrap.setDayOfWeekDisplay(dayOfWeek.getName(ParamUtils.getString(wrap.getDayOfWeek())));
            wrap.setStartTimeDisplay(LessonUtils.formatTime(wrap.getStartTime()));
            wrap.setEndTimeDisplay(LessonUtils.formatTime(wrap.getEndTime()));
            ClassroomCacheBean classroomBean = classroomProxy.queryById(wrap.getClassroomId());
            if (classroomBean != null) {
                wrap.setClassroomName(classroomBean.getName());
            }
            wrap.setFrequencyDisplay(lessonFrequency.getName(ParamUtils.getString(wrap.getFrequency())));
            wrap.setStartHour(LessonUtils.computeHour(wrap.getStartTime()));
            wrap.setStartMinute(LessonUtils.computeMinute(wrap.getStartTime()));
        }
        return lessonPlanWrapList;
    }

    public LessonPlanWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (LessonPlanWrap) wrappedList.get(0) : null;
    }
}
