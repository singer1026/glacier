package com.qinyin.athene.model;

public class AppTagRelation extends BaseModel {
    public static final String OBJECT_ID = "objectId";
    private Integer objectId;
    public static final String OBJECT_TYPE = "objectType";
    private String objectType;
    public static final String TAG_ID = "tagId";
    private Integer tagId;

    public static final String SQL_FILE_NAME = "AppTagRelation";

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

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}