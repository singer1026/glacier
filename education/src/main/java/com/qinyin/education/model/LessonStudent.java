package com.qinyin.education.model;

import com.qinyin.athene.model.BaseModel;

public class LessonStudent extends BaseModel {
    public static final String LESSON_ID = "lessonId";
    private Integer lessonId;
    public static final String STUDENT_ID = "studentId";
    private Integer studentId;
    public static final String STUDENT_NAME = "studentName";
    private String studentName;

    public static final String SQL_FILE_NAME = "EduLessonStudent";

    public LessonStudent() {
    }

    public LessonStudent(Integer studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}