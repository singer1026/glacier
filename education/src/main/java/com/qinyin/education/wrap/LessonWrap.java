/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.education.cache.model.LessonCacheBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class LessonWrap {
    public static Logger log = LoggerFactory.getLogger(LessonWrap.class);
    private Integer id;
    private Timestamp gmtCreate;
    private String gmtCreateDisplay;
    private String creator;
    private String creatorDisplay;
    private Timestamp gmtModify;
    private String gmtModifyDisplay;
    private String modifier;
    private String modifierDisplay;
    private Integer subjectId;
    private String subjectName;
    private String remark;
    private List lessonTeacherList;
    private List lessonStudentList;
    private List lessonPlanList;

    public LessonWrap() {
    }

    public LessonWrap(LessonCacheBean lessonCacheBean) {
        try {
            PropertyUtils.copyProperties(this, lessonCacheBean);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtCreateDisplay() {
        return gmtCreateDisplay;
    }

    public void setGmtCreateDisplay(String gmtCreateDisplay) {
        this.gmtCreateDisplay = gmtCreateDisplay;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorDisplay() {
        return creatorDisplay;
    }

    public void setCreatorDisplay(String creatorDisplay) {
        this.creatorDisplay = creatorDisplay;
    }

    public Timestamp getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Timestamp gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getGmtModifyDisplay() {
        return gmtModifyDisplay;
    }

    public void setGmtModifyDisplay(String gmtModifyDisplay) {
        this.gmtModifyDisplay = gmtModifyDisplay;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierDisplay() {
        return modifierDisplay;
    }

    public void setModifierDisplay(String modifierDisplay) {
        this.modifierDisplay = modifierDisplay;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List getLessonTeacherList() {
        return lessonTeacherList;
    }

    public void setLessonTeacherList(List lessonTeacherList) {
        this.lessonTeacherList = lessonTeacherList;
    }

    public List getLessonStudentList() {
        return lessonStudentList;
    }

    public void setLessonStudentList(List lessonStudentList) {
        this.lessonStudentList = lessonStudentList;
    }

    public List getLessonPlanList() {
        return lessonPlanList;
    }

    public void setLessonPlanList(List lessonPlanList) {
        this.lessonPlanList = lessonPlanList;
    }
}
