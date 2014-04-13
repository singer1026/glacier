/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.wrap;

import com.qinyin.customer.model.Task;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class TaskWrap {
    public static Logger log = LoggerFactory.getLogger(TaskWrap.class);
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
    private Timestamp notifyDate;
    private String notifyDateDisplay;
    private String title;
    private String description;
    private String disposer;
    private String disposerDisplay;
    private Timestamp disposeTime;
    private String disposeTimeDisplay;
    private String disposeOpinion;

    public TaskWrap() {
    }

    public TaskWrap(Task task) {
        try {
            PropertyUtils.copyProperties(this, task);
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

    public Timestamp getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Timestamp notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getNotifyDateDisplay() {
        return notifyDateDisplay;
    }

    public void setNotifyDateDisplay(String notifyDateDisplay) {
        this.notifyDateDisplay = notifyDateDisplay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisposer() {
        return disposer;
    }

    public void setDisposer(String disposer) {
        this.disposer = disposer;
    }

    public String getDisposerDisplay() {
        return disposerDisplay;
    }

    public void setDisposerDisplay(String disposerDisplay) {
        this.disposerDisplay = disposerDisplay;
    }

    public Timestamp getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Timestamp disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getDisposeTimeDisplay() {
        return disposeTimeDisplay;
    }

    public void setDisposeTimeDisplay(String disposeTimeDisplay) {
        this.disposeTimeDisplay = disposeTimeDisplay;
    }

    public String getDisposeOpinion() {
        return disposeOpinion;
    }

    public void setDisposeOpinion(String disposeOpinion) {
        this.disposeOpinion = disposeOpinion;
    }
}
