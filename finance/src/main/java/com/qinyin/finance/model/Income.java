package com.qinyin.finance.model;

import com.qinyin.athene.model.AdvanceModel;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Income extends AdvanceModel {
    public static final String COMPANY_ID = "companyId";
    private Integer companyId;
    public static final String CATEGORY = "category";
    private String category;
    public static final String TITLE = "title";
    private String title;
    public static final String DESCRIPTION = "description";
    private String description;
    public static final String INCOME_DATE = "incomeDate";
    private Timestamp incomeDate;
    public static final String AMOUNT = "amount";
    private BigDecimal amount;

    public static final String SQL_FILE_NAME = "FinIncome";

    public static final String FULL_EDIT_ROLE = "base_income_full_edit";

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

    public Timestamp getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Timestamp incomeDate) {
        this.incomeDate = incomeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}