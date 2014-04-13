/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-7
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

public class MaxInteger {
    private Integer count = null;

    public void setInteger(Integer i) {
        if (this.count == null || i > this.count) {
            this.count = i;
        }
    }

    public Integer getInteger() {
        return this.count;
    }
}
