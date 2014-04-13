package com.qinyin.customer.validation;

import com.qinyin.athene.model.DataInfoBean;

public interface TaskValidation {
    public DataInfoBean simpleValidate();

    public DataInfoBean approveValidate();
}
