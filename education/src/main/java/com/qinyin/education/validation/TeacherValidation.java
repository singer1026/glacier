package com.qinyin.education.validation;

import com.qinyin.athene.model.DataInfoBean;

public interface TeacherValidation {
    public DataInfoBean simpleValidate();

    public DataInfoBean saveValidate();

    public DataInfoBean updateValidate() throws Exception;

    public DataInfoBean deleteValidate() throws Exception;
}
