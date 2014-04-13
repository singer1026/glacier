package com.qinyin.athene.validation;

import com.qinyin.athene.model.DataInfoBean;

public interface UserValidation {
    public DataInfoBean simpleValidate();

    public DataInfoBean fullValidate();

    public DataInfoBean saveValidate();

    public DataInfoBean ownValidate();

    public DataInfoBean passwordValidate();
}
