/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.model;

import java.sql.Timestamp;

public class AdvanceModel extends BaseModel {
    public static final String GMT_CREATE = "gmtCreate";
    private Timestamp gmtCreate;
    public static final String CREATOR = "creator";
    private String creator;
    public static final String GMT_MODIFY = "gmtModify";
    private Timestamp gmtModify;
    public static final String MODIFIER = "modifier";
    private String modifier;
    public static final String IS_DELETED = "isDeleted";
    private String isDeleted;
    public static final String VERSION = "version";
    private Integer version;

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Timestamp gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
