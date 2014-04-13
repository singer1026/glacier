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
import com.qinyin.finance.model.Income;
import com.qinyin.finance.validation.IncomeValidation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class IncomeValidationImpl implements IncomeValidation {
    private PrivilegeInfo pvgInfo;

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = AtheneUtils.getInfo();
        String title = ParamUtils.getString(paramMap.get(Income.TITLE));
        if (title == null) {
            info.addValidationError("title-" + number + "-error", "必须输入标题");
        } else if (DefenseUtils.isUnlawfulChar(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.UNLAWFUL_CHAR);
        } else if (DefenseUtils.hasQuotes(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.HAS_QUOTES);
        }
        String amount = ParamUtils.getString(paramMap.get(Income.AMOUNT));
        if (amount == null) {
            info.addValidationError("amount-" + number + "-error", "必须输入金额");
        } else if (!ValidationUtils.isBigDecimal(amount)) {
            info.addValidationError("amount-" + number + "-error", "格式不正确,必须为数字");
        } else if (!ValidationUtils.isAllowBigDecimal(amount, 1)) {
            info.addValidationError("amount-" + number + "-error", "格式不正确,小数位最多为1位");
        } else {
            paramMap.put("amount", new BigDecimal(amount));
        }
        if (PermissionUtils.hasBaseRole(pvgInfo.getLoginId(), Income.FULL_EDIT_ROLE)) {
            String incomeDate = ParamUtils.getString(paramMap.get(Income.INCOME_DATE));
            if (incomeDate == null) {
                info.addValidationError("income-date-" + number + "-error", "必须输入收入日期");
            } else {
                Date _incomeDate = DateUtils.parseDate(incomeDate);
                if (_incomeDate == null) {
                    info.addValidationError("income-date-" + number + "-error", "日期格式不正确");
                } else {
                    paramMap.put(Income.INCOME_DATE, _incomeDate);
                }
            }
        }
        String category = ParamUtils.getString(paramMap.get(Income.CATEGORY));
        if (category == null) {
            info.addValidationError("category-" + number + "-error", "必须选择类目");
        }
        String description = ParamUtils.getString(paramMap.get(Income.DESCRIPTION));
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
