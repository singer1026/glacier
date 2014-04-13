/**
 * @author zhaolie
 * @create-time 2010-11-26
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.resource;

import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;

import java.util.Map;

public class SessionResource {

    @UrlMapping(value = "/keepSession", type = ReturnType.JSON)
    public Map<String, Object> keepSession() {
        return null;
    }
}

