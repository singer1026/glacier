/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.validation.impl;

import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.DefenseUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.customer.model.Task;
import com.qinyin.customer.validation.TaskValidation;

import java.util.Date;
import java.util.Map;

public class TaskValidationImpl implements TaskValidation {

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = AtheneUtils.getInfo();
        String title = ParamUtils.getString(paramMap.get(Task.TITLE));
        if (title == null) {
            info.addValidationError("title-" + number + "-error", "必须输入标题");
        } else if (DefenseUtils.isUnlawfulChar(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.UNLAWFUL_CHAR);
        } else if (DefenseUtils.hasQuotes(title)) {
            info.addValidationError("title-" + number + "-error", "标题" + DefenseUtils.HAS_QUOTES);
        }
        String notifyDate = ParamUtils.getString(paramMap.get(Task.NOTIFY_DATE));
        if (notifyDate == null) {
            info.addValidationError("notify-date-" + number + "-error", "必须输入最早提醒时间");
        } else {
            Date _notifyDate = DateUtils.parseDate(notifyDate);
            if (_notifyDate == null) {
                info.addValidationError("notify-date-" + number + "-error", "日期格式不正确");
            } else {
                paramMap.put("notifyDate", _notifyDate);
            }
        }
        String description = ParamUtils.getString(paramMap.get(Task.DESCRIPTION));
        if (description != null) {
            if (DefenseUtils.isUnlawfulChar(description)) {
                info.addValidationError("description-" + number + "-error", "整体描述" + DefenseUtils.UNLAWFUL_CHAR);
            }
        }
        return info;
    }

    public DataInfoBean approveValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = AtheneUtils.getInfo();
        String disposeOpinion = ParamUtils.getString(paramMap.get(Task.DISPOSE_OPINION));
        if (disposeOpinion != null) {
            if (DefenseUtils.isUnlawfulChar(disposeOpinion)) {
                info.addValidationError("dispose-opinion-" + number + "-error", "处理意见" + DefenseUtils.UNLAWFUL_CHAR);
            }
        }
        return info;
    }
}
