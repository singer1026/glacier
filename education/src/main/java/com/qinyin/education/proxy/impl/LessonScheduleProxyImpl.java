/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.education.cache.model.ClassroomCacheBean;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.proxy.ClassroomProxy;
import com.qinyin.education.proxy.LessonProxy;
import com.qinyin.education.proxy.LessonScheduleProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.service.LessonPlanService;
import com.qinyin.education.util.LessonUtils;
import com.qinyin.education.wrap.LessonWrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonScheduleProxyImpl implements LessonScheduleProxy {
    private LessonProxy lessonProxy;
    private SubjectProxy subjectProxy;
    private ClassroomProxy classroomProxy;
    private LessonPlanService lessonPlanService;

    @Override
    public Map<String, Object> queryTeacherScheduleByTeacherId(Integer id) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        List<LessonPlan> lessonPlanList = lessonPlanService.queryListByTeacherId(id);
        List<List<Map<String, Object>>> AMList = new ArrayList<List<Map<String, Object>>>(7);
        List<List<Map<String, Object>>> PMList = new ArrayList<List<Map<String, Object>>>(7);
        for (int i = 0; i < 7; i++) {
            AMList.add(new ArrayList<Map<String, Object>>());
            PMList.add(new ArrayList<Map<String, Object>>());
        }
        for (LessonPlan lessonPlan : lessonPlanList) {
            Map<String, Object> lessonPlanMap = new HashMap<String, Object>();
            List<Map<String, Object>> list = null;
            Integer dayOfWeek = lessonPlan.getDayOfWeek();
            if (LessonUtils.isAMLesson(lessonPlan.getStartTime())) {
                list = AMList.get(dayOfWeek);
            } else {
                list = PMList.get(dayOfWeek);
            }
            LessonWrap lessonWrap = lessonProxy.queryForWrap(lessonPlan.getLessonId());
            List<LessonStudent> lessonStudentList = lessonWrap.getLessonStudentList();
            if (lessonStudentList.size() > 1) {
                lessonPlanMap.put("type", "大课");
            } else {
                lessonPlanMap.put("type", "小课");
            }
            lessonPlanMap.put("timeLength", lessonPlan.getTimeLength());
            lessonPlanMap.put("startTimeDisplay", LessonUtils.formatTime(lessonPlan.getStartTime()));
            lessonPlanMap.put("endTimeDisplay", LessonUtils.formatTime(lessonPlan.getEndTime()));
            lessonPlanMap.put("subjectName", lessonWrap.getSubjectName());
            ClassroomCacheBean classroomCacheBean = classroomProxy.queryById(lessonPlan.getClassroomId());
            if (classroomCacheBean != null) {
                lessonPlanMap.put("classroomName", classroomCacheBean.getName());
            }
            lessonPlanMap.put("lessonStudentList", lessonStudentList);
            list.add(lessonPlanMap);
        }
        rtnMap.put("AMList", AMList);
        rtnMap.put("PMList", PMList);
        return rtnMap;
    }

    public void setLessonProxy(LessonProxy lessonProxy) {
        this.lessonProxy = lessonProxy;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }

    public void setClassroomProxy(ClassroomProxy classroomProxy) {
        this.classroomProxy = classroomProxy;
    }

    public void setLessonPlanService(LessonPlanService lessonPlanService) {
        this.lessonPlanService = lessonPlanService;
    }
}
