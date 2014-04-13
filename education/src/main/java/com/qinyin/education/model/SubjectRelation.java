package com.qinyin.education.model;

public class SubjectRelation {
    private Integer id;
    public static final String OBJECT_ID = "objectId";
    private Integer objectId;
    public static final String OBJECT_TYPE = "objectType";
    private String objectType;
    public static final String SUBJECT_ID = "subjectId";
    private Integer subjectId;

    public static final String SQL_FILE_NAME = "EduSubjectRelation";


    public SubjectRelation() {

    }

    public SubjectRelation(Integer objectId, String objectType) {
        this.objectId = objectId;
        this.objectType = objectType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }
}