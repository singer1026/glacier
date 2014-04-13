package com.qinyin.education.validation;

import com.qinyin.athene.model.DataInfoBean;

public interface StudentValidation {
    public DataInfoBean simpleValidate();

    public DataInfoBean saveValidate();

    public DataInfoBean updateValidate() throws Exception;

    public DataInfoBean deleteValidate() throws Exception;

    public DataInfoBean quitValidate() throws Exception;

    public DataInfoBean enterValidate() throws Exception;
}
