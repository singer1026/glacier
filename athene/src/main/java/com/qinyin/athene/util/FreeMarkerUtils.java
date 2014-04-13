/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-17
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerUtils {
    public static Map<String, Object> getInitMap(HttpServletRequest req) {
        Map<String, Object> data = new HashMap<String, Object>();
        StringBuilder basePath = new StringBuilder();
        basePath.append(req.getScheme()).append("://").append(req.getServerName());
        if (req.getServerPort() != 80) {
            basePath.append(":").append(req.getServerPort());
        }
        basePath.append(req.getContextPath()).append("/");
        data.put("basePath", basePath.toString());
        return data;
    }
}
