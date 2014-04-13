/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.education.model.LessonPlan;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LessonPlanWrap {
    public static Logger log = LoggerFactory.getLogger(LessonPlanWrap.class);
    private Integer dayOfWeek;
    private String dayOfWeekDisplay;
    private Integer startTime;
    private String startTimeDisplay;
    private Integer endTime;
    private String endTimeDisplay;
    private Integer timeLength;
    private Integer frequency;
    private String frequencyDisplay;
    private Integer classroomId;
    private String classroomName;
    private Integer startHour;
    private Integer startMinute;

    public LessonPlanWrap() {
    }

    public LessonPlanWrap(LessonPlan lessonPlan) {
        try {
            PropertyUtils.copyProperties(this, lessonPlan);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeekDisplay() {
        return dayOfWeekDisplay;
    }

    public void setDayOfWeekDisplay(String dayOfWeekDisplay) {
        this.dayOfWeekDisplay = dayOfWeekDisplay;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeDisplay() {
        return startTimeDisplay;
    }

    public void setStartTimeDisplay(String startTimeDisplay) {
        this.startTimeDisplay = startTimeDisplay;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeDisplay() {
        return endTimeDisplay;
    }

    public void setEndTimeDisplay(String endTimeDisplay) {
        this.endTimeDisplay = endTimeDisplay;
    }

    public Integer getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyDisplay() {
        return frequencyDisplay;
    }

    public void setFrequencyDisplay(String frequencyDisplay) {
        this.frequencyDisplay = frequencyDisplay;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }
}
