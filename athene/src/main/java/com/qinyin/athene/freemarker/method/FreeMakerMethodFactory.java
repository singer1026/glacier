/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-3-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.method;

public class FreeMakerMethodFactory {
    private HasBaseRoleMethod hasBaseRoleMethod;
    private HasRoleMethod hasRoleMethod;
    private GetResNameMethod getResNameMethod;

    public void init() {
        this.hasBaseRoleMethod = new HasBaseRoleMethod();
        this.hasRoleMethod = new HasRoleMethod();
        this.getResNameMethod = new GetResNameMethod();
    }

    public HasBaseRoleMethod getHasBaseRoleMethod() {
        return hasBaseRoleMethod;
    }

    public HasRoleMethod getHasRoleMethod() {
        return hasRoleMethod;
    }

    public GetResNameMethod getGetResNameMethod() {
        return getResNameMethod;
    }
}
