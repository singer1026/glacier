package com.qinyin.finance.model;

import com.qinyin.athene.model.AdvanceModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Expense extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String CATEGORY = "category";
    private String category;
    public static final String TITLE = "title";
    private String title;
    public static final String DESCRIPTION = "description";
    private String description;
    public static final String EXPEND_DATE = "expendDate";
    private Timestamp expendDate;
    public static final String AMOUNT = "amount";
    private BigDecimal amount;

    public static final String SQL_FILE_NAME = "FinExpense";

    public static final String FULL_EDIT_ROLE = "base_expense_full_edit";

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Timestamp getExpendDate() {
        return expendDate;
    }

    public void setExpendDate(Timestamp expendDate) {
        this.expendDate = expendDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}