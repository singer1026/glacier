/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.education.cache.model.StudentCacheBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class StudentWrap {
    public static Logger log = LoggerFactory.getLogger(StudentWrap.class);
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
    private Timestamp gmtEnter;
    private String gmtEnterDisplay;
    private Timestamp gmtQuit;
    private String gmtQuitDisplay;
    private String loginId;
    private String school;
    private String description;
    private List subjectList;

    public StudentWrap() {
    }

    public StudentWrap(UserCacheBean userCacheBean, StudentCacheBean studentCacheBean) {
        try {
            PropertyUtils.copyProperties(this, userCacheBean);
            PropertyUtils.copyProperties(this, studentCacheBean);
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

    public Timestamp getGmtEnter() {
        return gmtEnter;
    }

    public void setGmtEnter(Timestamp gmtEnter) {
        this.gmtEnter = gmtEnter;
    }

    public String getGmtEnterDisplay() {
        return gmtEnterDisplay;
    }

    public void setGmtEnterDisplay(String gmtEnterDisplay) {
        this.gmtEnterDisplay = gmtEnterDisplay;
    }

    public Timestamp getGmtQuit() {
        return gmtQuit;
    }

    public void setGmtQuit(Timestamp gmtQuit) {
        this.gmtQuit = gmtQuit;
    }

    public String getGmtQuitDisplay() {
        return gmtQuitDisplay;
    }

    public void setGmtQuitDisplay(String gmtQuitDisplay) {
        this.gmtQuitDisplay = gmtQuitDisplay;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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
