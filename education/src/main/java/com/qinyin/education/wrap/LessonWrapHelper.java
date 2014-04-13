/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.ClassroomCacheBean;
import com.qinyin.education.cache.model.LessonCacheBean;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.manager.StaticResourceManager;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.proxy.ClassroomProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.util.LessonUtils;

import java.util.ArrayList;
import java.util.List;

public class LessonWrapHelper {
    private SubjectProxy subjectProxy;
    private ClassroomProxy classroomProxy;
    private StaticResourceManager staticResourceManager;
    private ResourceCacheBean dayOfWeek;
    private ResourceCacheBean lessonFrequency;
    private List lessonWrapList = new ArrayList();
    private Privilege pvg;

    public LessonWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        subjectProxy = (SubjectProxy) BeanFactoryHolder.getBean("subjectProxy");
        classroomProxy = (ClassroomProxy) BeanFactoryHolder.getBean("classroomProxy");
        staticResourceManager = (StaticResourceManager) BeanFactoryHolder.getBean("staticResourceManager");
        dayOfWeek = resourceProxy.getCommonBean("dayOfWeek");
        lessonFrequency = resourceProxy.getCompanyBean("lessonFrequency");
    }

    public void addWrap(LessonCacheBean lessonCacheBean) {
        if (lessonCacheBean == null) return;
        lessonWrapList.add(new LessonWrap(lessonCacheBean));
    }

    public List getWrappedForList() {
        for (Object obj : lessonWrapList) {
            LessonWrap wrap = (LessonWrap) obj;
            SubjectCacheBean subjectCacheBean = subjectProxy.queryById(wrap.getSubjectId());
            if (subjectCacheBean != null) {
                wrap.setSubjectName(subjectCacheBean.getName());
            }
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
            List lessonPlanList = wrap.getLessonPlanList();
            List lessonPlanWrapList = new ArrayList();
            for (Object object : lessonPlanList) {
                LessonPlanWrap lessonPlanWrap = new LessonPlanWrap((LessonPlan) object);
                lessonPlanWrap.setDayOfWeekDisplay(dayOfWeek.getName(ParamUtils.getString(lessonPlanWrap.getDayOfWeek())));
                lessonPlanWrap.setStartTimeDisplay(LessonUtils.formatTime(lessonPlanWrap.getStartTime()));
                lessonPlanWrap.setFrequencyDisplay(lessonFrequency.getName(lessonPlanWrap.getFrequency().toString()));
                lessonPlanWrap.setEndTimeDisplay(LessonUtils.formatTime(lessonPlanWrap.getEndTime()));
                lessonPlanWrap.setStartHour(LessonUtils.computeHour(lessonPlanWrap.getStartTime()));
                lessonPlanWrap.setStartMinute(LessonUtils.computeMinute(lessonPlanWrap.getStartTime()));
                ClassroomCacheBean classroomBean = classroomProxy.queryById(lessonPlanWrap.getClassroomId());
                if (classroomBean != null) {
                    lessonPlanWrap.setClassroomName(classroomBean.getName());
                }
                lessonPlanWrapList.add(lessonPlanWrap);
            }
            wrap.setLessonPlanList(lessonPlanWrapList);
        }
        return lessonWrapList;
    }

    public LessonWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (LessonWrap) wrappedList.get(0) : null;
    }
}
