/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import java.util.Map;

public class MapUtils {
    public static void removeProperties(Map src, Object... objects) {
        if (objects != null && src != null) {
            for (Object obj : objects) {
                src.remove(obj);
            }
        }
    }
}
