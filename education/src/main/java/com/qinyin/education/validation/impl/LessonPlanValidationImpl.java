/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.validation.impl;

import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.util.LessonUtils;
import com.qinyin.education.validation.LessonPlanValidation;

import java.util.Map;

public class LessonPlanValidationImpl implements LessonPlanValidation {
    @Override
    public DataInfoBean validate() {
        DataInfoBean info = AtheneUtils.getInfo();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        Integer dayOfWeek = ParamUtils.getInteger(paramMap.get(LessonPlan.DAY_OF_WEEK));
        if (dayOfWeek == null) {
            info.addValidationError("day-of-week-" + number + "-error", "必须选择周期");
        } else if (dayOfWeek < 0 || dayOfWeek > 6) {
            info.addValidationError("day-of-week-" + number + "-error", "星期超出范围");
        }
        Integer startHour = ParamUtils.getInteger(paramMap.get("startHour"));
        Integer startMinute = ParamUtils.getInteger(paramMap.get("startMinute"));
        if (startHour == null) {
            info.addValidationError("start-time-" + number + "-error", "必须选择开始小时");
        } else if (startHour < 0 || startHour > 23) {
            info.addValidationError("start-time-" + number + "-error", "小时超出范围");
        } else if (startMinute == null) {
            info.addValidationError("start-time-" + number + "-error", "必须选择开始分钟");
        } else if (startMinute < 0 || startMinute > 59) {
            info.addValidationError("start-time-" + number + "-error", "分钟超出范围");
        }
        Integer timeLength = ParamUtils.getInteger(paramMap.get(LessonPlan.TIME_LENGTH));
        if (timeLength == null) {
            info.addValidationError("time-length-" + number + "-error", "必须选择时长");
        }
        Integer frequency = ParamUtils.getInteger(paramMap.get(LessonPlan.FREQUENCY));
        if (frequency == null) {
            info.addValidationError("frequency-" + number + "-error", "必须选择频率");
        }
        if (info.hasError()) return info;
        int startTime = LessonUtils.generateTime(dayOfWeek, startHour, startMinute);
        int endTime = LessonUtils.generateEndTime(startTime, timeLength);
        if (startTime < 0 || startTime > LessonUtils.MINUTE_OF_WEEK) {
            info.addValidationError("start-time-" + number + "-error", "开始时间超出范围");
        } else if (endTime < 0 || endTime > LessonUtils.MINUTE_OF_WEEK) {
            info.addValidationError("start-time-" + number + "-error", "结束时间超出范围");
        } else if (!LessonUtils.isSameDay(startTime, endTime)) {
            info.addValidationError("start-time-" + number + "-error", "开始时间和结束时间不在同一天");
        }
        if (info.hasError()) return info;
        LessonPlan lessonPlan = new LessonPlan();
        lessonPlan.setDayOfWeek(dayOfWeek);
        lessonPlan.setStartTime(startTime);
        lessonPlan.setEndTime(endTime);
        lessonPlan.setTimeLength(timeLength);
        lessonPlan.setFrequency(frequency);
        lessonPlan.setClassroomId(ParamUtils.getIdInteger(paramMap.get("classroomId")));
        info.setData(lessonPlan);
        return info;
    }
}
