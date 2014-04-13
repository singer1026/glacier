/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.education.cache.model.TeacherCacheBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class TeacherWrap {
    public static Logger log = LoggerFactory.getLogger(TeacherWrap.class);
    private String name;
    private String sex;
    private String sexDisplay;
    private String address;
    private String qq;
    private String email;
    private String telephone;
    private String mobile;
    /**
     * Teacher
     */
    private Integer id;
    private Timestamp gmtCreate;
    private String gmtCreateDisplay;
    private String creator;
    private String creatorDisplay;
    private Timestamp gmtModify;
    private String gmtModifyDisplay;
    private String modifier;
    private String modifierDisplay;
    private String status;
    private String statusDisplay;
    private String loginId;
    private String graduateSchool;
    private String education;
    private String educationDisplay;
    private String level;
    private String levelDisplay;
    private String major;
    private String experience;
    private String experienceDisplay;
    private String description;
    private List subjectList;

    public TeacherWrap() {
    }

    public TeacherWrap(UserCacheBean userCacheBean, TeacherCacheBean teacherCacheBean) {
        try {
            PropertyUtils.copyProperties(this, userCacheBean);
            PropertyUtils.copyProperties(this, teacherCacheBean);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexDisplay() {
        return sexDisplay;
    }

    public void setSexDisplay(String sexDisplay) {
        this.sexDisplay = sexDisplay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationDisplay() {
        return educationDisplay;
    }

    public void setEducationDisplay(String educationDisplay) {
        this.educationDisplay = educationDisplay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelDisplay() {
        return levelDisplay;
    }

    public void setLevelDisplay(String levelDisplay) {
        this.levelDisplay = levelDisplay;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperienceDisplay() {
        return experienceDisplay;
    }

    public void setExperienceDisplay(String experienceDisplay) {
        this.experienceDisplay = experienceDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List subjectList) {
        this.subjectList = subjectList;
    }
}
