/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.system.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SystemResource {
    public static Logger log = LoggerFactory.getLogger(SystemResource.class);

    @UrlMapping(value = "/systemManagerList", type = ReturnType.FREEMARKER, url = "system/systemManagerList.ftl")
    public Map<String, Object> managerList(@Args Map<String, Object> paramMap) {
        return paramMap;
    }
}
