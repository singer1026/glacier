/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.validation.impl;

import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.*;
import com.qinyin.finance.model.Expense;
import com.qinyin.finance.validation.ExpenseValidation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ExpenseValidationImpl implements ExpenseValidation {
    private PrivilegeInfo pvgInfo;

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = AtheneUtils.getInfo();
        String title = ParamUtils.getString(paramMap.get(Expense.TITLE));
        if (title == null) {
            info.addValidationError("title-" + number + "-error", "必须输入标题");
        } else if (DefenseUtils.isUnlawfulChar(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.UNLAWFUL_CHAR);
        } else if (DefenseUtils.hasQuotes(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.HAS_QUOTES);
        }
        String amount = ParamUtils.getString(paramMap.get(Expense.AMOUNT));
        if (amount == null) {
            info.addValidationError("amount-" + number + "-error", "必须输入金额");
        } else if (!ValidationUtils.isBigDecimal(amount)) {
            info.addValidationError("amount-" + number + "-error", "格式不正确,必须为数字");
        } else if (!ValidationUtils.isAllowBigDecimal(amount, 1)) {
            info.addValidationError("amount-" + number + "-error", "格式不正确,小数位最多为1位");
        } else {
            paramMap.put(Expense.AMOUNT, new BigDecimal(amount));
        }
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Expense.FULL_EDIT_ROLE)) {
            String expendDate = ParamUtils.getString(paramMap.get(Expense.EXPEND_DATE));
            if (expendDate == null) {
                info.addValidationError("expend-date-" + number + "-error", "必须输入支出日期");
            } else {
                Date _expendDate = DateUtils.parseDate(expendDate);
                if (_expendDate == null) {
                    info.addValidationError("expend-date-" + number + "-error", "日期格式不正确");
                } else {
                    paramMap.put(Expense.EXPEND_DATE, _expendDate);
                }
            }
        }
        String category = ParamUtils.getString(paramMap.get(Expense.CATEGORY));
        if (category == null) {
            info.addValidationError("category-" + number + "-error", "必须选择类目");
        }
        String description = ParamUtils.getString(paramMap.get(Expense.DESCRIPTION));
        if (description != null) {
            if (DefenseUtils.isUnlawfulChar(description)) {
                info.addValidationError("description-" + number + "-error", "整体描述" + DefenseUtils.UNLAWFUL_CHAR);
            }
        }
        return info;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
