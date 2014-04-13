package com.qinyin.education.model;

import com.qinyin.athene.model.BaseModel;

public class LessonTeacher extends BaseModel {
    public static final String LESSON_ID = "lessonId";
    private Integer lessonId;
    public static final String TEACHER_ID = "teacherId";
    private Integer teacherId;
    public static final String TEACHER_NAME = "teacherName";
    private String teacherName;

    public static final String SQL_FILE_NAME = "EduLessonTeacher";

    public LessonTeacher() {
    }

    public LessonTeacher(Integer teacherId, String teacherName) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}