package com.qinyin.education.validation;

import com.qinyin.athene.model.DataInfoBean;

public interface LessonValidation {
    public DataInfoBean simpleValidate();

    public DataInfoBean deepValidate() throws Exception;
}
