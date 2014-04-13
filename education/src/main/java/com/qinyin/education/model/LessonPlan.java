package com.qinyin.education.model;

import com.qinyin.athene.model.BaseModel;

public class LessonPlan extends BaseModel {
    public static final String LESSON_ID = "lessonId";
    private Integer lessonId;
    public static final String DAY_OF_WEEK = "dayOfWeek";
    private Integer dayOfWeek;
    public static final String START_TIME = "startTime";
    private Integer startTime;
    public static final String END_TIME = "endTime";
    private Integer endTime;
    public static final String TIME_LENGTH = "timeLength";
    private Integer timeLength;
    public static final String FREQUENCY = "frequency";
    private Integer frequency;
    public static final String CLASSROOM_ID = "classroomId";
    private Integer classroomId;

    public static final String SQL_FILE_NAME = "EduLessonPlan";

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
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

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
    }
}