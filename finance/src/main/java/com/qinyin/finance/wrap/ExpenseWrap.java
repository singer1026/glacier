/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.wrap;

import com.qinyin.athene.util.DateUtils;
import com.qinyin.finance.model.Expense;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ExpenseWrap {
    public static Logger log = LoggerFactory.getLogger(ExpenseWrap.class);
    private Integer id;
    private Timestamp gmtCreate;
    private String gmtCreateDisplay;
    private String creator;
    private String creatorDisplay;
    private Timestamp gmtModify;
    private String gmtModifyDisplay;
    private String modifier;
    private String modifierDisplay;
    private String category;
    private String categoryDisplay;
    private String title;
    private String titleDisplay;
    private String description;
    private Timestamp expendDate;
    private String expendDateDisplay;
    private BigDecimal amount;
    private String amountDisplay;

    public ExpenseWrap() {
    }

    public ExpenseWrap(Expense expense) {
        try {
            PropertyUtils.copyProperties(this, expense);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryDisplay() {
        return categoryDisplay;
    }

    public void setCategoryDisplay(String categoryDisplay) {
        this.categoryDisplay = categoryDisplay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleDisplay() {
        return titleDisplay;
    }

    public void setTitleDisplay(String titleDisplay) {
        this.titleDisplay = titleDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getExpendDate() {
        return expendDate;
    }

    public void setExpendDate(Timestamp expendDate) {
        this.expendDate = expendDate;
    }

    public String getExpendDateDisplay() {
        return expendDateDisplay;
    }

    public void setExpendDateDisplay(String expendDateDisplay) {
        this.expendDateDisplay = expendDateDisplay;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountDisplay() {
        return amountDisplay;
    }

    public void setAmountDisplay(String amountDisplay) {
        this.amountDisplay = amountDisplay;
    }

    public boolean getIsToday() {
        return DateUtils.isToday(expendDate);
    }
}
