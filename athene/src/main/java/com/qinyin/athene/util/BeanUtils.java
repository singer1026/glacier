/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-2
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanUtils {
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Map) return (Map<String, Object>) obj;
        Map<String, Object> map = new HashMap<String, Object>();
        Method[] methods = obj.getClass().getMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("get") && !StringUtils.equals(methodName, "getClass")) {
                    String filedName = methodName.substring("get".length());
                    try {
                        Object result = method.invoke(obj, null);
                        if (result != null) {
                            map.put(StringUtils.uncapitalize(filedName), result);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return map;
    }

    public static Object toBean(Map<String, Object> map, Class clazz) {
        if (map == null || map.size() == 0) return null;
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
        }
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Method method = clazz.getMethod("set" + StringUtils.capitalize(key), value.getClass());
                if (method != null) {
                    method.invoke(obj, value);
                }
            } catch (Exception e) {
            }
        }
        return obj;
    }
}
